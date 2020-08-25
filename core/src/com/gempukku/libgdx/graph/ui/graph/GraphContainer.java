package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.graph.data.GraphValidator;
import com.gempukku.libgdx.graph.data.NodeConnector;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.preview.NavigableCanvas;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisWindow;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphContainer<T extends FieldType> extends WidgetGroup implements NavigableCanvas {
    private static final float CANVAS_GAP = 50f;
    private static final float CONNECTOR_LENGTH = 10;
    private static final float CONNECTOR_RADIUS = 5;

    private float canvasX;
    private float canvasY;
    private float canvasWidth;
    private float canvasHeight;
    private boolean navigating;

    private Map<String, GraphBox<T>> graphBoxes = new HashMap<>();
    private Map<String, VisWindow> boxWindows = new HashMap<>();
    private List<GraphConnection> graphConnections = new LinkedList<>();

    private Map<NodeConnector, Shape> connectionNodeMap = new HashMap<>();
    private Map<GraphConnection, Shape> connections = new HashMap<>();

    private ShapeRenderer shapeRenderer;

    private NodeConnector drawingFromConnector;
    private GraphValidator.ValidationResult<GraphBox<T>, GraphConnection, PropertyBox<T>, T> validationResult = new GraphValidator.ValidationResult<>();

    public GraphContainer(final PopupMenuProducer popupMenuProducer) {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

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

    public void centerCanvas() {
        navigateTo((canvasWidth - getWidth()) / 2f, (canvasHeight - getHeight()) / 2f);
    }

    @Override
    public void getCanvasPosition(Vector2 result) {
        result.set(canvasX, canvasY);
    }

    @Override
    public void getCanvasSize(Vector2 result) {
        result.set(canvasWidth, canvasHeight);
    }

    @Override
    public void getVisibleSize(Vector2 result) {
        result.set(getWidth(), getHeight());
    }

    @Override
    public void navigateTo(float x, float y) {
        x = MathUtils.round(x);
        y = MathUtils.round(y);

        navigating = true;
        float difX = x - canvasX;
        float difY = y - canvasY;
        for (Actor element : getElements()) {
            element.moveBy(-difX, -difY);
        }
        canvasX = x;
        canvasY = y;
        navigating = false;
    }

    @Override
    public Array<Actor> getElements() {
        return getChildren();
    }

    private void updateCanvas(boolean adjustPosition) {
        if (!navigating) {
            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = Float.MIN_VALUE;
            float maxY = Float.MIN_VALUE;

            Array<Actor> children = getElements();
            if (children.size == 0) {
                minX = 0;
                minY = 0;
                maxX = 0;
                maxY = 0;
            } else {
                for (Actor child : children) {
                    float childX = child.getX();
                    float childY = child.getY();
                    float childWidth = child.getWidth();
                    float childHeight = child.getHeight();
                    minX = Math.min(minX, childX);
                    minY = Math.min(minY, childY);
                    maxX = Math.max(maxX, childX + childWidth);
                    maxY = Math.max(maxY, childY + childHeight);
                }
            }

            minX -= CANVAS_GAP;
            minY -= CANVAS_GAP;
            maxX += CANVAS_GAP;
            maxY += CANVAS_GAP;

            canvasWidth = maxX - minX;
            canvasHeight = maxY - minY;

            if (adjustPosition) {
                canvasX = -minX;
                canvasY = -minY;
            }
        }
    }

    public void adjustCanvas() {
        updateCanvas(true);
    }

    public void setValidationResult(GraphValidator.ValidationResult<GraphBox<T>, GraphConnection, PropertyBox<T>, T> validationResult) {
        this.validationResult = validationResult;
        for (GraphBox<T> value : graphBoxes.values()) {
            VisWindow window = boxWindows.get(value.getId());
            if (validationResult.getErrorNodes().contains(value)) {
                window.getTitleLabel().setColor(Color.RED);
            } else if (validationResult.getWarningNodes().contains(value)) {
                window.getTitleLabel().setColor(Color.GOLDENROD);
            } else {
                window.getTitleLabel().setColor(Color.WHITE);
            }
        }
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

        for (Map.Entry<GraphConnection, Shape> connectionEntry : connections.entrySet()) {
            if (connectionEntry.getValue().contains(x, y)) {
                GraphConnection connection = connectionEntry.getKey();
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

    private void removeConnection(GraphConnection connection) {
        graphConnections.remove(connection);
        fire(new GraphChangedEvent(true));
        invalidate();
    }

    private void processNodeClick(NodeConnector clickedNodeConnector) {
        GraphBox<T> clickedNode = getGraphBoxById(clickedNodeConnector.getNodeId());
        if (drawingFromConnector != null) {
            if (!drawingFromConnector.equals(clickedNodeConnector)) {
                GraphBox<T> drawingFromNode = getGraphBoxById(drawingFromConnector.getNodeId());

                boolean drawingFromIsInput = drawingFromNode.isInputField(drawingFromConnector.getFieldId());
                if (drawingFromIsInput == clickedNode.isInputField(clickedNodeConnector.getFieldId())) {
                    drawingFromConnector = null;
                } else {
                    NodeConnector connectorFrom = drawingFromIsInput ? clickedNodeConnector : drawingFromConnector;
                    NodeConnector connectorTo = drawingFromIsInput ? drawingFromConnector : clickedNodeConnector;

                    GraphNodeOutput<T> output = getGraphBoxById(connectorFrom.getNodeId()).getOutputs().get(connectorFrom.getFieldId());
                    GraphNodeInput<T> input = getGraphBoxById(connectorTo.getNodeId()).getInputs().get(connectorTo.getFieldId());

                    if (!connectorsMatch(input, output)) {
                        // Either input-input, output-output, or different property type
                        drawingFromConnector = null;
                    } else {
                        // Remove conflicting connections if needed
                        for (GraphConnection oldConnection : findNodeConnections(connectorTo)) {
                            removeConnection(oldConnection);
                        }
                        if (!output.supportsMultiple()) {
                            for (GraphConnection oldConnection : findNodeConnections(connectorFrom)) {
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
                    || !clickedNode.getOutputs().get(clickedNodeConnector.getFieldId()).supportsMultiple()) {
                List<GraphConnection> nodeConnections = findNodeConnections(clickedNodeConnector);
                if (nodeConnections.size() > 0) {
                    GraphConnection oldConnection = nodeConnections.get(0);
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

    private boolean connectorsMatch(GraphNodeInput<T> input, GraphNodeOutput<T> output) {
        List<? extends T> producablePropertyTypes = output.getProducablePropertyTypes();
        for (T acceptedPropertyType : input.getAcceptedPropertyTypes()) {
            if (producablePropertyTypes.contains(acceptedPropertyType))
                return true;
        }

        return false;
    }

    private List<GraphConnection> findNodeConnections(NodeConnector nodeConnector) {
        String nodeId = nodeConnector.getNodeId();
        String fieldId = nodeConnector.getFieldId();

        List<GraphConnection> result = new LinkedList<>();
        for (GraphConnection graphConnection : graphConnections) {
            if ((graphConnection.getNodeFrom().equals(nodeId) && graphConnection.getFieldFrom().equals(fieldId))
                    || (graphConnection.getNodeTo().equals(nodeId) && graphConnection.getFieldTo().equals(fieldId)))
                result.add(graphConnection);
        }
        return result;
    }

    public void addGraphBox(final GraphBox<T> graphBox, String windowTitle, boolean closeable, float x, float y) {
        graphBoxes.put(graphBox.getId(), graphBox);
        VisWindow window = new VisWindow(windowTitle) {
            @Override
            protected void positionChanged() {
                recreateClickableShapes();
                updateCanvas(true);
                fire(new GraphChangedEvent(false));
            }

            @Override
            protected void close() {
                removeGraphBox(graphBox);
                super.close();
            }
        };
        window.setKeepWithinStage(false);
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

    private void removeGraphBox(GraphBox<T> graphBox) {
        Iterator<GraphConnection> graphConnectionIterator = graphConnections.iterator();
        while (graphConnectionIterator.hasNext()) {
            GraphConnection graphConnectionImpl = graphConnectionIterator.next();
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
        graphConnections.add(new GraphConnectionImpl(fromNode, fromField, toNode, toField));
        fire(new GraphChangedEvent(true));
        invalidate();
    }

    @Override
    public void layout() {
        super.layout();
        updateShadeRenderer();
        recreateClickableShapes();
        updateCanvas(false);
    }

    private void recreateClickableShapes() {
        connectionNodeMap.clear();
        connections.clear();

        Vector2 from = new Vector2();
        for (Map.Entry<String, VisWindow> windowEntry : boxWindows.entrySet()) {
            String nodeId = windowEntry.getKey();
            Window window = windowEntry.getValue();
            GraphBox<T> graphBox = graphBoxes.get(nodeId);
            float windowX = window.getX();
            float windowY = window.getY();
            for (GraphBoxInputConnector<T> connector : graphBox.getInputs().values()) {
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
            for (GraphBoxOutputConnector<T> connector : graphBox.getOutputs().values()) {
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
        for (GraphConnection graphConnection : graphConnections) {
            NodeConnector fromNode = getNodeInfo(graphConnection.getNodeFrom(), graphConnection.getFieldFrom());
            Window fromWindow = boxWindows.get(fromNode.getNodeId());
            GraphBoxOutputConnector<T> output = getGraphBoxById(fromNode.getNodeId()).getOutputs().get(fromNode.getFieldId());
            calculateConnection(from, fromWindow, output);
            NodeConnector toNode = getNodeInfo(graphConnection.getNodeTo(), graphConnection.getFieldTo());
            Window toWindow = boxWindows.get(toNode.getNodeId());
            GraphBoxInputConnector<T> input = getGraphBoxById(toNode.getNodeId()).getInputs().get(toNode.getFieldId());
            calculateConnection(to, toWindow, input);

            Shape shape = basicStroke.createStrokedShape(new Line2D.Float(from.x, from.y, to.x, to.y));

            connections.put(graphConnection, shape);
        }
    }

    private NodeConnector getNodeInfo(String nodeId, String fieldId) {
        GraphBox<T> graphBox = graphBoxes.get(nodeId);
        if (graphBox.getInputs().get(fieldId) != null || graphBox.getOutputs().get(fieldId) != null)
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
        shapeRenderer.setColor(Color.WHITE);

        for (Map.Entry<String, VisWindow> windowEntry : boxWindows.entrySet()) {
            String nodeId = windowEntry.getKey();
            Window window = windowEntry.getValue();
            GraphBox<T> graphBox = graphBoxes.get(nodeId);
            for (GraphBoxInputConnector<T> connector : graphBox.getInputs().values()) {
                if (!connector.isRequired()) {
                    calculateConnector(from, to, window, connector);
                    from.add(x, y);
                    to.add(x, y);

                    shapeRenderer.line(from, to);
                    shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
                }
            }

            for (GraphBoxOutputConnector<T> connector : graphBox.getOutputs().values()) {
                calculateConnector(from, to, window, connector);
                from.add(x, y);
                to.add(x, y);

                shapeRenderer.line(from, to);
                shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
            }
        }

        for (GraphConnection graphConnection : graphConnections) {
            NodeConnector fromNode = getNodeInfo(graphConnection.getNodeFrom(), graphConnection.getFieldFrom());
            Window fromWindow = boxWindows.get(fromNode.getNodeId());
            GraphBoxOutputConnector<T> output = getGraphBoxById(fromNode.getNodeId()).getOutputs().get(fromNode.getFieldId());
            calculateConnection(from, fromWindow, output);
            NodeConnector toNode = getNodeInfo(graphConnection.getNodeTo(), graphConnection.getFieldTo());
            Window toWindow = boxWindows.get(toNode.getNodeId());
            GraphBoxInputConnector<T> input = getGraphBoxById(toNode.getNodeId()).getInputs().get(toNode.getFieldId());
            calculateConnection(to, toWindow, input);

            boolean error = validationResult.getErrorConnections().contains(graphConnection);
            shapeRenderer.setColor(error ? Color.RED : Color.WHITE);

            from.add(x, y);
            to.add(x, y);

            shapeRenderer.line(from, to);
        }

        if (drawingFromConnector != null) {
            shapeRenderer.setColor(Color.WHITE);
            GraphBox<T> drawingFromNode = getGraphBoxById(drawingFromConnector.getNodeId());
            Window fromWindow = getBoxWindow(drawingFromConnector.getNodeId());
            if (drawingFromNode.isInputField(drawingFromConnector.getFieldId())) {
                GraphBoxInputConnector<T> input = drawingFromNode.getInputs().get(drawingFromConnector.getFieldId());
                calculateConnection(from, fromWindow, input);
                shapeRenderer.line(x + from.x, y + from.y, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            } else {
                GraphBoxOutputConnector<T> output = drawingFromNode.getOutputs().get(drawingFromConnector.getFieldId());
                calculateConnection(from, fromWindow, output);
                shapeRenderer.line(x + from.x, y + from.y, Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            }
        }

        shapeRenderer.set(ShapeRenderer.ShapeType.Filled);

        for (Map.Entry<String, VisWindow> windowEntry : boxWindows.entrySet()) {
            String nodeId = windowEntry.getKey();
            Window window = windowEntry.getValue();
            GraphBox<T> graphBox = graphBoxes.get(nodeId);
            for (GraphBoxInputConnector<T> connector : graphBox.getInputs().values()) {
                if (connector.isRequired()) {
                    calculateConnector(from, to, window, connector);
                    from.add(x, y);
                    to.add(x, y);

                    boolean isErrorous = false;
                    for (NodeConnector errorConnector : validationResult.getErrorConnectors()) {
                        if (errorConnector.getNodeId().equals(nodeId) && errorConnector.getFieldId().equals(connector.getFieldId())) {
                            isErrorous = true;
                            break;
                        }
                    }
                    shapeRenderer.setColor(isErrorous ? Color.RED : Color.WHITE);

                    shapeRenderer.line(from, to);
                    shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
                }
            }
        }
        shapeRenderer.end();
    }

    private void calculateConnector(Vector2 from, Vector2 to, Window window, GraphBoxOutputConnector<T> connector) {
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

    private void calculateConnector(Vector2 from, Vector2 to, Window window, GraphBoxInputConnector<T> connector) {
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

    public GraphBox<T> getGraphBoxById(String id) {
        return graphBoxes.get(id);
    }

    public List<GraphConnection> getIncomingConnections(GraphBox<T> graphBox) {
        List<GraphConnection> result = new LinkedList<>();
        for (GraphConnection graphConnection : graphConnections) {
            if (graphConnection.getNodeTo().equals(graphBox.getId()))
                result.add(graphConnection);
        }
        return result;
    }

    public Iterable<GraphBox<T>> getGraphBoxes() {
        return graphBoxes.values();
    }

    public Iterable<? extends GraphConnection> getConnections() {
        return graphConnections;
    }

    private void updateShadeRenderer() {
        shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.updateMatrices();
    }

    private void calculateConnection(Vector2 position, Window window, GraphBoxInputConnector<T> connector) {
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

    private void calculateConnection(Vector2 position, Window window, GraphBoxOutputConnector<T> connector) {
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
