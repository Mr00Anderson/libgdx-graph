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
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInput;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutput;
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

    private Map<String, GraphBox> graphBoxes = new HashMap<>();
    private Map<String, Window> boxWindows = new HashMap<>();
    private List<GraphConnectionImpl> graphConnectionImpls = new LinkedList<>();

    private Map<NodeConnector, Shape> connectionNodeMap = new HashMap<>();
    private Map<GraphConnectionImpl, Shape> connections = new HashMap<>();

    private ShapeRenderer shapeRenderer;

    private NodeConnector drawingFromConnector;

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

        for (Map.Entry<NodeConnector, Shape> nodeEntry : connectionNodeMap.entrySet()) {
            if (nodeEntry.getValue().contains(x, y)) {
                processNodeClick(nodeEntry.getKey());
                return;
            }
        }

        for (Map.Entry<GraphConnectionImpl, Shape> connectionEntry : connections.entrySet()) {
            if (connectionEntry.getValue().contains(x, y)) {
                GraphConnectionImpl connection = connectionEntry.getKey();
                removeConnection(connection);
                return;
            }
        }

        drawingFromConnector = null;
    }

    private boolean containedInWindow(float x, float y) {
        for (Window window : boxWindows.values()) {
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

    private void removeConnection(GraphConnectionImpl connection) {
        graphConnectionImpls.remove(connection);
        fire(new GraphChangedEvent(true));
        invalidate();
    }

    private void processNodeClick(NodeConnector clickedNodeConnector) {
        GraphBox clickedNode = getGraphBoxById(clickedNodeConnector.getNodeId());
        if (drawingFromConnector != null) {
            if (!drawingFromConnector.equals(clickedNodeConnector)) {
                GraphBox drawingFromNode = getGraphBoxById(drawingFromConnector.getNodeId());

                boolean drawingFromIsInput = drawingFromNode.isInputField(drawingFromConnector.getFieldId());
                if (drawingFromIsInput == clickedNode.isInputField(clickedNodeConnector.getFieldId())) {
                    drawingFromConnector = null;
                } else {
                    NodeConnector connectorFrom = drawingFromIsInput ? drawingFromConnector : clickedNodeConnector;
                    NodeConnector connectorTo = drawingFromIsInput ? clickedNodeConnector : drawingFromConnector;

                    PipelineNodeOutput output = getGraphBoxById(connectorFrom.getNodeId()).getOutput(connectorFrom.getFieldId());
                    PipelineNodeInput input = getGraphBoxById(connectorTo.getNodeId()).getInput(connectorTo.getFieldId());

                    if (!connectorsMatch(input, output)) {
                        // Either input-input, output-output, or different property type
                        drawingFromConnector = null;
                    } else {
                        // Remove conflicting connections if needed
                        for (GraphConnectionImpl oldConnection : findNodeConnections(connectorTo)) {
                            removeConnection(oldConnection);
                        }
                        if (!output.supportsMultiple()) {
                            for (GraphConnectionImpl oldConnection : findNodeConnections(connectorFrom)) {
                                removeConnection(oldConnection);
                            }
                        }
                        addGraphConnection(connectorFrom.getNodeId(), connectorFrom.getFieldId(),
                                connectorTo.getNodeId(), connectorTo.getFieldId());
                        drawingFromConnector = null;
                    }
                }
            } else {
                // Same node, that started at
                drawingFromConnector = null;
            }
        } else {
            if (clickedNode.isInputField(clickedNodeConnector.getFieldId())
                    || !clickedNode.getOutput(clickedNodeConnector.getFieldId()).supportsMultiple()) {
                List<GraphConnectionImpl> nodeConnections = findNodeConnections(clickedNodeConnector);
                if (nodeConnections.size() > 0) {
                    GraphConnectionImpl oldConnection = nodeConnections.get(0);
                    removeConnection(oldConnection);
                    NodeConnector oldNode = getNodeInfo(oldConnection.getNodeFrom(), oldConnection.getFieldFrom());
                    if (oldNode.equals(clickedNodeConnector))
                        drawingFromConnector = getNodeInfo(oldConnection.getNodeTo(), oldConnection.getFieldTo());
                    else
                        drawingFromConnector = oldNode;
                } else {
                    drawingFromConnector = clickedNodeConnector;
                }
            } else {
                drawingFromConnector = clickedNodeConnector;
            }
        }
    }

    private boolean connectorsMatch(PipelineNodeInput input, PipelineNodeOutput output) {
        for (PropertyType value : PropertyType.values()) {
            if (input.getPropertyType().getAcceptedPropertyTypes().contains(value)
                    && output.getPropertyType().mayProduce(value))
                return true;
        }
        return false;
    }

    private List<GraphConnectionImpl> findNodeConnections(NodeConnector nodeConnector) {
        String nodeId = nodeConnector.getNodeId();
        String fieldId = nodeConnector.getFieldId();

        List<GraphConnectionImpl> result = new LinkedList<>();
        for (GraphConnectionImpl graphConnectionImpl : graphConnectionImpls) {
            if ((graphConnectionImpl.getNodeFrom().equals(nodeId) && graphConnectionImpl.getFieldFrom().equals(fieldId))
                    || (graphConnectionImpl.getNodeTo().equals(nodeId) && graphConnectionImpl.getFieldTo().equals(fieldId)))
                result.add(graphConnectionImpl);
        }
        return result;
    }

    public void addGraphBox(final GraphBox graphBox, String windowTitle, boolean closeable, float x, float y) {
        graphBoxes.put(graphBox.getId(), graphBox);
        VisWindow window = new VisWindow(windowTitle) {
            @Override
            protected void positionChanged() {
                recreateClickableShapes();
                fire(new GraphChangedEvent(false));
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
        boxWindows.put(graphBox.getId(), window);
        fire(new GraphChangedEvent(true));
    }

    private void removeGraphBox(GraphBox graphBox) {
        Iterator<GraphConnectionImpl> graphConnectionIterator = graphConnectionImpls.iterator();
        while (graphConnectionIterator.hasNext()) {
            GraphConnectionImpl graphConnectionImpl = graphConnectionIterator.next();
            if (graphConnectionImpl.getNodeFrom().equals(graphBox.getId())
                    || graphConnectionImpl.getNodeTo().equals(graphBox.getId()))
                graphConnectionIterator.remove();
        }

        boxWindows.remove(graphBox.getId());
        graphBoxes.remove(graphBox.getId());
        fire(new GraphChangedEvent(true));
    }

    public void addGraphConnection(String fromNode, String fromField, String toNode, String toField) {
        NodeConnector nodeFrom = getNodeInfo(fromNode, fromField);
        NodeConnector nodeTo = getNodeInfo(toNode, toField);
        if (nodeFrom == null || nodeTo == null)
            throw new IllegalArgumentException("Can't find node");
        graphConnectionImpls.add(new GraphConnectionImpl(fromNode, fromField, toNode, toField));
        fire(new GraphChangedEvent(true));
        invalidate();
    }

    @Override
    public void layout() {
        super.layout();
        recreateClickableShapes();
    }

    private void recreateClickableShapes() {
        connectionNodeMap.clear();
        connections.clear();

        Vector2 from = new Vector2();
        for (Map.Entry<String, Window> windowEntry : boxWindows.entrySet()) {
            String nodeId = windowEntry.getKey();
            Window window = windowEntry.getValue();
            GraphBox graphBox = graphBoxes.get(nodeId);
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

                connectionNodeMap.put(new NodeConnector(nodeId, connector.getFieldId()), rectangle);
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

                connectionNodeMap.put(new NodeConnector(nodeId, connector.getFieldId()), rectangle);
            }
        }

        BasicStroke basicStroke = new BasicStroke(7);
        Vector2 to = new Vector2();
        for (GraphConnectionImpl graphConnectionImpl : graphConnectionImpls) {
            NodeConnector fromNode = getNodeInfo(graphConnectionImpl.getNodeFrom(), graphConnectionImpl.getFieldFrom());
            Window fromWindow = boxWindows.get(fromNode.getNodeId());
            GraphBoxOutputConnector output = getGraphBoxById(fromNode.getNodeId()).getOutput(fromNode.getFieldId());
            calculateConnection(from, fromWindow, output);
            NodeConnector toNode = getNodeInfo(graphConnectionImpl.getNodeTo(), graphConnectionImpl.getFieldTo());
            Window toWindow = boxWindows.get(toNode.getNodeId());
            GraphBoxInputConnector input = getGraphBoxById(toNode.getNodeId()).getInput(toNode.getFieldId());
            calculateConnection(to, toWindow, input);

            Shape shape = basicStroke.createStrokedShape(new Line2D.Float(from.x, from.y, to.x, to.y));

            connections.put(graphConnectionImpl, shape);
        }
    }

    private NodeConnector getNodeInfo(String nodeId, String fieldId) {
        GraphBox graphBox = graphBoxes.get(nodeId);
        if (graphBox.getInput(fieldId) != null || graphBox.getOutput(fieldId) != null)
            return new NodeConnector(nodeId, fieldId);
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

        for (Map.Entry<String, Window> windowEntry : boxWindows.entrySet()) {
            String nodeId = windowEntry.getKey();
            Window window = windowEntry.getValue();
            GraphBox graphBox = graphBoxes.get(nodeId);
            for (GraphBoxInputConnector connector : graphBox.getGraphBoxInputConnectors()) {
                if (!connector.isRequired()) {
                    calculateConnector(from, to, window, connector);
                    from.add(x, y);
                    to.add(x, y);

                    shapeRenderer.line(from, to);
                    shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
                }
            }

            for (GraphBoxOutputConnector connector : graphBox.getGraphBoxOutputConnectors()) {
                calculateConnector(from, to, window, connector);
                from.add(x, y);
                to.add(x, y);

                shapeRenderer.line(from, to);
                shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
            }
        }

        for (GraphConnectionImpl graphConnectionImpl : graphConnectionImpls) {
            NodeConnector fromNode = getNodeInfo(graphConnectionImpl.getNodeFrom(), graphConnectionImpl.getFieldFrom());
            Window fromWindow = boxWindows.get(fromNode.getNodeId());
            GraphBoxOutputConnector output = getGraphBoxById(fromNode.getNodeId()).getOutput(fromNode.getFieldId());
            calculateConnection(from, fromWindow, output);
            NodeConnector toNode = getNodeInfo(graphConnectionImpl.getNodeTo(), graphConnectionImpl.getFieldTo());
            Window toWindow = boxWindows.get(toNode.getNodeId());
            GraphBoxInputConnector input = getGraphBoxById(toNode.getNodeId()).getInput(toNode.getFieldId());
            calculateConnection(to, toWindow, input);

            from.add(x, y);
            to.add(x, y);

            shapeRenderer.line(from, to);
        }

        if (drawingFromConnector != null) {
            GraphBox drawingFromNode = getGraphBoxById(drawingFromConnector.getNodeId());
            Window fromWindow = getBoxWindow(drawingFromConnector.getNodeId());
            if (drawingFromNode.isInputField(drawingFromConnector.getFieldId())) {
                GraphBoxInputConnector input = drawingFromNode.getInput(drawingFromConnector.getFieldId());
                calculateConnection(from, fromWindow, input);
                shapeRenderer.line(x + from.x, y + from.y, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            } else {
                GraphBoxOutputConnector output = drawingFromNode.getOutput(drawingFromConnector.getFieldId());
                calculateConnection(from, fromWindow, output);
                shapeRenderer.line(x + from.x, y + from.y, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            }
        }

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Map.Entry<String, Window> windowEntry : boxWindows.entrySet()) {
            String nodeId = windowEntry.getKey();
            Window window = windowEntry.getValue();
            GraphBox graphBox = graphBoxes.get(nodeId);
            for (GraphBoxInputConnector connector : graphBox.getGraphBoxInputConnectors()) {
                if (connector.isRequired()) {
                    calculateConnector(from, to, window, connector);
                    from.add(x, y);
                    to.add(x, y);

                    shapeRenderer.line(from, to);
                    shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
                }
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
        return graphBoxes.get(id);
    }

    public List<GraphConnectionImpl> getIncomingConnections(GraphBox graphBox) {
        List<GraphConnectionImpl> result = new LinkedList<>();
        for (GraphConnectionImpl graphConnectionImpl : graphConnectionImpls) {
            if (graphConnectionImpl.getNodeTo().equals(graphBox.getId()))
                result.add(graphConnectionImpl);
        }
        return result;
    }

    public Iterable<GraphBox> getGraphBoxes() {
        return graphBoxes.values();
    }

    public Iterable<? extends GraphConnectionImpl> getConnections() {
        return graphConnectionImpls;
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

    public Window getBoxWindow(String nodeId) {
        return boxWindows.get(nodeId);
    }
}
