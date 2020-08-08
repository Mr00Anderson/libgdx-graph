package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisWindow;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphContainer extends WidgetGroup {
    private static final float CONNECTOR_LENGTH = 10;
    private static final float CONNECTOR_RADIUS = 5;

    private List<GraphBox> graphBoxes = new LinkedList<>();
    private Map<GraphBox, Window> graphBoxWindowMap = new HashMap<>();
    private List<GraphConnection> graphConnections = new LinkedList<>();

    private Map<String, Shape> connectionNodeMap = new HashMap<>();
    private Map<GraphConnection, Shape> connections = new HashMap<>();

    private ShapeRenderer shapeRenderer;

    private NodeInfo drawingFrom;

    public GraphContainer(final PopupMenuProducer popupMenuProducer) {
        shapeRenderer = new ShapeRenderer();

        addListener(
                new ClickListener(Input.Buttons.RIGHT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (!containedInWindow(x, y)) {
                            PopupMenu popupMenu = popupMenuProducer.createPopupMenu(x, y);
                            popupMenu.showMenu(getStage(), x + getX(), y + getY());
                        }
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
        for (Window window : graphBoxWindowMap.values()) {
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
        fire(new GraphChangedEvent());
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

    public void addGraphBox(final GraphBox graphBox, String windowTitle, boolean closeable, float x, float y) {
        graphBoxes.add(graphBox);
        VisWindow window = new VisWindow(windowTitle) {
            @Override
            protected void positionChanged() {
                recreateClickableShapes();
                fire(new GraphChangedEvent());
            }

            @Override
            protected void close() {
                removeGraphBox(graphBox);
                super.close();
            }
        };
        window.setKeepWithinParent(true);
        if (closeable) {
            window.addCloseButton();
        }
        window.add(graphBox.getActor()).grow().row();
        window.setPosition(x, y);
        addActor(window);
        window.setSize(Math.max(150, window.getPrefWidth()), window.getPrefHeight());
        graphBoxWindowMap.put(graphBox, window);
        fire(new GraphChangedEvent());
    }

    private void removeGraphBox(GraphBox graphBox) {
        Iterator<GraphConnection> graphConnectionIterator = graphConnections.iterator();
        while (graphConnectionIterator.hasNext()) {
            GraphConnection graphConnection = graphConnectionIterator.next();
            if (graphConnection.getFrom().getGraphBox() == graphBox
                    || graphConnection.getTo().getGraphBox() == graphBox)
                graphConnectionIterator.remove();
        }

        graphBoxWindowMap.remove(graphBox);
        graphBoxes.remove(graphBox);
    }

    public void addGraphConnection(String from, String to) {
        NodeInfo nodeFrom = getNodeInfo(from);
        NodeInfo nodeTo = getNodeInfo(to);
        if (nodeFrom == null || nodeTo == null)
            throw new IllegalArgumentException("Can't find node");
        graphConnections.add(new GraphConnection(nodeFrom, nodeTo));
        fire(new GraphChangedEvent());
        invalidate();
    }

    @Override
    public void layout() {
        super.layout();
        System.out.println("Layout");
        recreateClickableShapes();
    }

    private void recreateClickableShapes() {
        connectionNodeMap.clear();
        connections.clear();

        Vector2 from = new Vector2();
        for (Map.Entry<GraphBox, Window> graphBoxWindowEntry : graphBoxWindowMap.entrySet()) {
            GraphBox graphBox = graphBoxWindowEntry.getKey();
            Window window = graphBoxWindowEntry.getValue();
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
            calculateConnection(from, graphBoxWindowMap.get(fromNode.getGraphBox()), fromNode.getOutputConnector());
            NodeInfo toNode = graphConnection.getTo();
            calculateConnection(to, graphBoxWindowMap.get(toNode.getGraphBox()), toNode.getInputConnector());

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

        for (Map.Entry<GraphBox, Window> graphBoxWindowEntry : graphBoxWindowMap.entrySet()) {
            GraphBox graphBox = graphBoxWindowEntry.getKey();
            Window window = graphBoxWindowEntry.getValue();
            for (GraphBoxInputConnector connector : graphBox.getGraphBoxInputConnectors()) {
                calculateConnector(from, to, window, connector);
                from.add(x, y);
                to.add(x, y);

                shapeRenderer.line(from, to);
                shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
            }

            for (GraphBoxOutputConnector connector : graphBox.getGraphBoxOutputConnectors()) {
                calculateConnector(from, to, window, connector);
                from.add(x, y);
                to.add(x, y);

                shapeRenderer.line(from, to);
                shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
            }
        }

        for (GraphConnection graphConnection : graphConnections) {
            NodeInfo fromNode = graphConnection.getFrom();
            calculateConnection(from, graphBoxWindowMap.get(fromNode.getGraphBox()), fromNode.getOutputConnector());
            NodeInfo toNode = graphConnection.getTo();
            calculateConnection(to, graphBoxWindowMap.get(toNode.getGraphBox()), toNode.getInputConnector());

            from.add(x, y);
            to.add(x, y);

            shapeRenderer.line(from, to);
        }

        if (drawingFrom != null) {
            if (drawingFrom.isInput()) {
                calculateConnection(from, graphBoxWindowMap.get(drawingFrom.getGraphBox()), drawingFrom.getInputConnector());
                shapeRenderer.line(x + from.x, y + from.y, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            } else {
                calculateConnection(from, graphBoxWindowMap.get(drawingFrom.getGraphBox()), drawingFrom.getOutputConnector());
                shapeRenderer.line(x + from.x, y + from.y, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            }
        }

        shapeRenderer.end();
    }

    private void calculateConnector(Vector2 from, Vector2 to, Window window, GraphBoxOutputConnector connector) {
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

    private void calculateConnector(Vector2 from, Vector2 to, Window window, GraphBoxInputConnector connector) {
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

    public GraphBox getGraphBoxById(String id) {
        for (GraphBox graphBox : graphBoxes) {
            if (graphBox.getId().equals(id))
                return graphBox;
        }
        return null;
    }

    public List<GraphConnection> getIncomingConnections(GraphBox graphBox) {
        List<GraphConnection> result = new LinkedList<>();
        for (GraphConnection graphConnection : graphConnections) {
            if (graphConnection.getTo().getGraphBox() == graphBox)
                result.add(graphConnection);
        }
        return result;
    }

    public Iterable<Map.Entry<GraphBox, Window>> getGraphBoxes() {
        return graphBoxWindowMap.entrySet();
    }

    public Iterable<? extends GraphConnection> getConnections() {
        return graphConnections;
    }

    public void resize(int width, int height) {
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
        shapeRenderer.updateMatrices();
    }

    private void calculateConnection(Vector2 position, Window window, GraphBoxInputConnector connector) {
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

    private void calculateConnection(Vector2 position, Window window, GraphBoxOutputConnector connector) {
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
