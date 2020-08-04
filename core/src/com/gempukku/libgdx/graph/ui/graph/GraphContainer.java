package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.graph.pipeline.producer.part.MergeBoxProducer;
import com.gempukku.graph.pipeline.producer.part.SplitBoxProducer;
import com.gempukku.graph.pipeline.producer.provided.ScreenSizeBoxProducer;
import com.gempukku.graph.pipeline.producer.provided.TimeBoxProducer;
import com.gempukku.graph.pipeline.producer.value.ValueBooleanBoxProducer;
import com.gempukku.graph.pipeline.producer.value.ValueColorBoxProducer;
import com.gempukku.graph.pipeline.producer.value.ValueVector1BoxProducer;
import com.gempukku.graph.pipeline.producer.value.ValueVector2BoxProducer;
import com.gempukku.graph.pipeline.producer.value.ValueVector3BoxProducer;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GraphContainer extends WidgetGroup {
    private Map<String, GraphBoxProducer> valueProducers = new LinkedHashMap<>();
    private Map<String, GraphBoxProducer> providedProducers = new LinkedHashMap<>();
    private Map<String, GraphBoxProducer> mathProducers = new LinkedHashMap<>();

    private static final float CONNECTOR_LENGTH = 10;
    private static final float CONNECTOR_RADIUS = 5;

    private List<GraphBox> graphBoxes = new LinkedList<>();
    private List<GraphConnection> graphConnections = new LinkedList<>();

    private Map<String, Shape> connectionNodeMap = new HashMap<>();
    private Map<GraphConnection, Shape> connections = new HashMap<>();

    private ShapeRenderer shapeRenderer;
    private Skin skin;

    private NodeInfo drawingFrom;

    public GraphContainer(Skin skin) {
        this.skin = skin;

        valueProducers.put("Color", new ValueColorBoxProducer());
        valueProducers.put("Vector1", new ValueVector1BoxProducer());
        valueProducers.put("Vector2", new ValueVector2BoxProducer());
        valueProducers.put("Vector3", new ValueVector3BoxProducer());
        valueProducers.put("Boolean", new ValueBooleanBoxProducer());

        providedProducers.put("Time", new TimeBoxProducer());
        providedProducers.put("Screen Size", new ScreenSizeBoxProducer());

        mathProducers.put("Split", new SplitBoxProducer());
        mathProducers.put("Merge", new MergeBoxProducer());

        shapeRenderer = new ShapeRenderer();

        addListener(
                new ClickListener(Input.Buttons.RIGHT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (!containedInWindow(x, y))
                            createPopup(x, y);
                    }
                });
        addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        processLeftClick(x, y);
                    }
                });
    }

    private void processLeftClick(float x, float y) {
        if (containedInWindow(x, y))
            return;

        for (Map.Entry<String, Shape> nodeEntry : connectionNodeMap.entrySet()) {
            if (nodeEntry.getValue().contains(x, y)) {
                processNodeClick(nodeEntry.getKey());
                return;
            }
        }

        for (Map.Entry<GraphConnection, Shape> connectionEntry : connections.entrySet()) {
            if (connectionEntry.getValue().contains(x, y)) {
                GraphConnection connection = connectionEntry.getKey();
                removeConnection(connection);
                return;
            }
        }

        drawingFrom = null;
    }

    private boolean containedInWindow(float x, float y) {
        for (GraphBox graphBox : graphBoxes) {
            Window window = graphBox.getWindow();
            float x1 = window.getX();
            float y1 = window.getY();
            float width = window.getWidth();
            float height = window.getHeight();
            // If window contains it - return
            if (x >= x1 && x < x1 + width
                    && y >= y1 && y < y1 + height)
                return true;
        }
        return false;
    }

    private void removeConnection(GraphConnection connection) {
        graphConnections.remove(connection);
        invalidate();
    }

    private void processNodeClick(String key) {
        if (drawingFrom != null) {
            NodeInfo nodeInfo = getNodeInfo(key);
            if (!drawingFrom.equals(nodeInfo)) {
                if (drawingFrom.isInput() == nodeInfo.isInput()) {
                    drawingFrom = null;
                } else {
                    GraphBoxInputConnector inputConnector = drawingFrom.isInput() ? drawingFrom.getInputConnector() : nodeInfo.getInputConnector();
                    GraphBoxOutputConnector outputConnector = drawingFrom.isInput() ? nodeInfo.getOutputConnector() : drawingFrom.getOutputConnector();
                    if (!inputConnector.accepts(outputConnector.getPropertyType())) {
                        // Either input-input, output-output, or different property type
                        drawingFrom = null;
                    } else {
                        // Remove conflicting connections if needed
                        for (GraphConnection oldConnection : findNodeConnections(inputConnector.getId())) {
                            removeConnection(oldConnection);
                        }
                        if (!outputConnector.getPropertyType().supportsMultiple()) {
                            for (GraphConnection oldConnection : findNodeConnections(outputConnector.getId())) {
                                removeConnection(oldConnection);
                            }
                        }
                        addGraphConnection(outputConnector.getId(), inputConnector.getId());
                        drawingFrom = null;
                    }
                }
            } else {
                // Same node, that started at
                drawingFrom = null;
            }
        } else {
            NodeInfo clickedNode = getNodeInfo(key);
            if (clickedNode.isInput()
                    || !clickedNode.getOutputConnector().getPropertyType().supportsMultiple()) {
                String id = clickedNode.isInput() ? clickedNode.getInputConnector().getId() : clickedNode.getOutputConnector().getId();
                List<GraphConnection> nodeConnections = findNodeConnections(id);
                if (nodeConnections.size() > 0) {
                    GraphConnection oldConnection = nodeConnections.get(0);
                    removeConnection(oldConnection);
                    if (oldConnection.getFrom().equals(clickedNode))
                        drawingFrom = oldConnection.getTo();
                    else
                        drawingFrom = oldConnection.getFrom();
                } else {
                    drawingFrom = clickedNode;
                }
            } else {
                drawingFrom = clickedNode;
            }
        }
    }

    private List<GraphConnection> findNodeConnections(String id) {
        List<GraphConnection> result = new LinkedList<>();
        for (GraphConnection graphConnection : graphConnections) {
            if (graphConnection.getFrom().getOutputConnector().getId().equals(id)
                    || graphConnection.getTo().getInputConnector().getId().equals(id))
                result.add(graphConnection);
        }
        return result;
    }

    private void createPopup(final float popupX, final float popupY) {
        PopupMenu popupMenu = new PopupMenu();

        createSubMenu(popupX, popupY, popupMenu, "Values", this.valueProducers);
        createSubMenu(popupX, popupY, popupMenu, "Provided", providedProducers);
        createSubMenu(popupX, popupY, popupMenu, "Math", mathProducers);

        popupMenu.showMenu(getStage(), popupX + getX(), popupY + getY());
    }

    private void createSubMenu(final float popupX, final float popupY, PopupMenu popupMenu, String menuName, Map<String, GraphBoxProducer> producerMap) {
        MenuItem valuesMenuItem = new MenuItem(menuName);
        PopupMenu valuesMenu = new PopupMenu();
        for (Map.Entry<String, GraphBoxProducer> valueEntry : producerMap.entrySet()) {
            String name = valueEntry.getKey();
            final GraphBoxProducer value = valueEntry.getValue();
            MenuItem valueMenuItem = new MenuItem(name);
            valueMenuItem.addListener(
                    new ClickListener(Input.Buttons.LEFT) {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            String id = UUID.randomUUID().toString().replace("-", "");
                            GraphBox graphBox = value.createDefault(skin, id, popupX, popupY);
                            addGraphBox(graphBox);
                        }
                    });
            valuesMenu.addItem(valueMenuItem);
        }
        valuesMenuItem.setSubMenu(valuesMenu);
        popupMenu.addItem(valuesMenuItem);
    }

    public void addGraphBox(GraphBox graphBox) {
        graphBoxes.add(graphBox);
        Window window = graphBox.getWindow();
        addActor(window);
        window.setSize(Math.max(150, window.getPrefWidth()), window.getPrefHeight());
        invalidate();
    }

    public void addGraphConnection(String from, String to) {
        NodeInfo nodeFrom = getNodeInfo(from);
        NodeInfo nodeTo = getNodeInfo(to);
        if (nodeFrom == null || nodeTo == null)
            throw new IllegalArgumentException("Can't find node");
        graphConnections.add(new GraphConnection(nodeFrom, nodeTo));
        invalidate();
    }

    @Override
    public void layout() {
        super.layout();
        fire(GraphChangedEvent.INSTANCE);

        connectionNodeMap.clear();
        connections.clear();

        Vector2 from = new Vector2();
        for (GraphBox graphBox : graphBoxes) {
            Window window = graphBox.getWindow();
            float windowX = window.getX();
            float windowY = window.getY();
            for (GraphBoxInputConnector connector : graphBox.getGraphBoxInputConnectors()) {
                switch (connector.getSide()) {
                    case Left:
                        from.set(windowX - CONNECTOR_LENGTH, windowY + connector.getOffset());
                        break;
                    case Top:
                        from.set(windowX + connector.getOffset(), windowY + window.getHeight() + CONNECTOR_LENGTH);
                        break;
                }
                Rectangle2D rectangle = new Rectangle2D.Float(
                        from.x - CONNECTOR_RADIUS, from.y - CONNECTOR_RADIUS,
                        CONNECTOR_RADIUS * 2, CONNECTOR_RADIUS * 2);

                connectionNodeMap.put(connector.getId(), rectangle);
            }
            for (GraphBoxOutputConnector connector : graphBox.getGraphBoxOutputConnectors()) {
                switch (connector.getSide()) {
                    case Right:
                        from.set(windowX + window.getWidth() + CONNECTOR_LENGTH, windowY + connector.getOffset());
                        break;
                    case Bottom:
                        from.set(windowX + connector.getOffset(), windowY - CONNECTOR_LENGTH);
                        break;
                }
                Rectangle2D rectangle = new Rectangle2D.Float(
                        from.x - CONNECTOR_RADIUS, from.y - CONNECTOR_RADIUS,
                        CONNECTOR_RADIUS * 2, CONNECTOR_RADIUS * 2);

                connectionNodeMap.put(connector.getId(), rectangle);
            }
        }

        BasicStroke basicStroke = new BasicStroke(7);
        Vector2 to = new Vector2();
        for (GraphConnection graphConnection : graphConnections) {
            NodeInfo fromNode = graphConnection.getFrom();
            calculateConnection(from, fromNode.getGraphBox(), fromNode.getOutputConnector());
            NodeInfo toNode = graphConnection.getTo();
            calculateConnection(to, toNode.getGraphBox(), toNode.getInputConnector());

            Shape shape = basicStroke.createStrokedShape(new Line2D.Float(from.x, from.y, to.x, to.y));

            connections.put(graphConnection, shape);
        }
    }

    private NodeInfo getNodeInfo(String id) {
        for (GraphBox graphBox : graphBoxes) {
            for (GraphBoxInputConnector connector : graphBox.getGraphBoxInputConnectors()) {
                if (connector.getId().equals(id))
                    return new NodeInfo(graphBox, connector);
            }
            for (GraphBoxOutputConnector connector : graphBox.getGraphBoxOutputConnectors()) {
                if (connector.getId().equals(id))
                    return new NodeInfo(graphBox, connector);
            }
        }
        return null;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        validate();
        batch.end();
        drawConnections();
        batch.begin();
        super.draw(batch, parentAlpha);
    }

    private void drawConnections() {
        float x = getX();
        float y = getY();

        Vector2 from = new Vector2();
        Vector2 to = new Vector2();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        for (GraphBox graphBox : graphBoxes) {
            for (GraphBoxInputConnector connector : graphBox.getGraphBoxInputConnectors()) {
                calculateConnector(from, to, graphBox, connector);
                from.add(x, y);
                to.add(x, y);

                shapeRenderer.line(from, to);
                shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
            }

            for (GraphBoxOutputConnector connector : graphBox.getGraphBoxOutputConnectors()) {
                calculateConnector(from, to, graphBox, connector);
                from.add(x, y);
                to.add(x, y);

                shapeRenderer.line(from, to);
                shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
            }
        }

        for (GraphConnection graphConnection : graphConnections) {
            NodeInfo fromNode = graphConnection.getFrom();
            calculateConnection(from, fromNode.getGraphBox(), fromNode.getOutputConnector());
            NodeInfo toNode = graphConnection.getTo();
            calculateConnection(to, toNode.getGraphBox(), toNode.getInputConnector());

            from.add(x, y);
            to.add(x, y);

            shapeRenderer.line(from, to);
        }

        if (drawingFrom != null) {
            if (drawingFrom.isInput()) {
                calculateConnection(from, drawingFrom.getGraphBox(), drawingFrom.getInputConnector());
                shapeRenderer.line(x + from.x, y + from.y, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            } else {
                calculateConnection(from, drawingFrom.getGraphBox(), drawingFrom.getOutputConnector());
                shapeRenderer.line(x + from.x, y + from.y, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            }
        }

        shapeRenderer.end();
    }

    private void calculateConnector(Vector2 from, Vector2 to, GraphBox graphBox, GraphBoxOutputConnector connector) {
        Window window = graphBox.getWindow();
        float windowX = window.getX();
        float windowY = window.getY();
        switch (connector.getSide()) {
            case Right:
                from.set(windowX + window.getWidth() + CONNECTOR_LENGTH, windowY + connector.getOffset());
                to.set(windowX + window.getWidth(), windowY + connector.getOffset());
                break;
            case Bottom:
                from.set(windowX + connector.getOffset(), windowY - CONNECTOR_LENGTH);
                to.set(windowX + connector.getOffset(), windowY);
                break;
        }
    }

    private void calculateConnector(Vector2 from, Vector2 to, GraphBox graphBox, GraphBoxInputConnector connector) {
        Window window = graphBox.getWindow();
        float windowX = window.getX();
        float windowY = window.getY();
        switch (connector.getSide()) {
            case Left:
                from.set(windowX - CONNECTOR_LENGTH, windowY + connector.getOffset());
                to.set(windowX, windowY + connector.getOffset());
                break;
            case Top:
                from.set(windowX + connector.getOffset(), windowY + window.getHeight() + CONNECTOR_LENGTH);
                to.set(windowX + connector.getOffset(), windowY + window.getHeight());
                break;
        }
    }

    public Iterable<? extends GraphBox> getGraphBoxes() {
        return graphBoxes;
    }

    public Iterable<? extends GraphConnection> getConnections() {
        return graphConnections;
    }

    public void resize(int width, int height) {
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        shapeRenderer.updateMatrices();
    }

    private void calculateConnection(Vector2 position, GraphBox graphBox, GraphBoxInputConnector connector) {
        Window window = graphBox.getWindow();
        float windowX = window.getX();
        float windowY = window.getY();
        switch (connector.getSide()) {
            case Left:
                position.set(windowX - CONNECTOR_LENGTH, windowY + connector.getOffset());
                break;
            case Top:
                position.set(windowX + connector.getOffset(), windowY + window.getHeight() + CONNECTOR_LENGTH);
                break;
        }
    }

    private void calculateConnection(Vector2 position, GraphBox graphBox, GraphBoxOutputConnector connector) {
        Window window = graphBox.getWindow();
        float windowX = window.getX();
        float windowY = window.getY();
        switch (connector.getSide()) {
            case Right:
                position.set(windowX + window.getWidth() + CONNECTOR_LENGTH, windowY + connector.getOffset());
                break;
            case Bottom:
                position.set(windowX + connector.getOffset(), windowY - CONNECTOR_LENGTH);
                break;
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
