package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import java.util.LinkedList;
import java.util.List;

public class GraphContainer extends WidgetGroup {
    private static final float CONNECTOR_LENGTH = 10;
    private static final float CONNECTOR_RADIUS = 5;

    private List<GraphBox> graphBoxes = new LinkedList<>();
    private List<GraphConnection> graphConnections = new LinkedList<>();

    private ShapeRenderer shapeRenderer;

    public GraphContainer(Skin skin) {
        shapeRenderer = new ShapeRenderer();
    }

    public void addGraphBox(GraphBox graphBox) {
        graphBoxes.add(graphBox);
        Window window = graphBox.getWindow();
        addActor(window);
        window.setSize(Math.max(150, window.getPrefWidth()), window.getPrefHeight());
        invalidate();
    }

    public void addGraphConnection(String from, String to) {
        graphConnections.add(new GraphConnection(from, to));
        invalidate();
    }

    @Override
    public void layout() {
        super.layout();
        fire(GraphChangedEvent.INSTANCE);
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
            for (GraphBoxConnector graphBoxConnector : graphBox.getGraphBoxConnectors()) {
                calculateConnector(from, to, graphBox, graphBoxConnector);
                from.add(x, y);
                to.add(x, y);

                shapeRenderer.line(from, to);
                shapeRenderer.circle(from.x, from.y, CONNECTOR_RADIUS);
            }
        }

        for (GraphConnection graphConnection : graphConnections) {
            for (GraphBox graphBox : graphBoxes) {
                GraphBoxConnector graphBoxConnectorFrom = graphBox.getGraphBoxConnector(graphConnection.getFrom());
                if (graphBoxConnectorFrom != null)
                    calculateConnection(from, graphBox, graphBoxConnectorFrom);
                GraphBoxConnector graphBoxConnectorTo = graphBox.getGraphBoxConnector(graphConnection.getTo());
                if (graphBoxConnectorTo != null)
                    calculateConnection(to, graphBox, graphBoxConnectorTo);
            }

            from.add(x, y);
            to.add(x, y);

            shapeRenderer.line(from, to);
        }

        shapeRenderer.end();
    }

    private void calculateConnector(Vector2 from, Vector2 to, GraphBox graphBox, GraphBoxConnector graphBoxConnector) {
        Window window = graphBox.getWindow();
        float windowX = window.getX();
        float windowY = window.getY();
        switch (graphBoxConnector.getSide()) {
            case Left:
                from.set(windowX - CONNECTOR_LENGTH, windowY + graphBoxConnector.getOffset());
                to.set(windowX, windowY + graphBoxConnector.getOffset());
                break;
            case Top:
                from.set(windowX + graphBoxConnector.getOffset(), windowY + window.getHeight() + CONNECTOR_LENGTH);
                to.set(windowX + graphBoxConnector.getOffset(), windowY + window.getHeight());
                break;
            case Right:
                from.set(windowX + window.getWidth() + CONNECTOR_LENGTH, windowY + graphBoxConnector.getOffset());
                to.set(windowX + window.getWidth(), windowY + graphBoxConnector.getOffset());
                break;
            case Bottom:
                from.set(windowX + graphBoxConnector.getOffset(), windowY - CONNECTOR_LENGTH);
                to.set(windowX + graphBoxConnector.getOffset(), windowY);
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

    private void calculateConnection(Vector2 position, GraphBox graphBox, GraphBoxConnector graphBoxConnector) {
        Window window = graphBox.getWindow();
        float windowX = window.getX();
        float windowY = window.getY();
        switch (graphBoxConnector.getSide()) {
            case Left:
                position.set(windowX - CONNECTOR_LENGTH, windowY + graphBoxConnector.getOffset());
                break;
            case Top:
                position.set(windowX + graphBoxConnector.getOffset(), windowY + window.getHeight() + CONNECTOR_LENGTH);
                break;
            case Right:
                position.set(windowX + window.getWidth() + CONNECTOR_LENGTH, windowY + graphBoxConnector.getOffset());
                break;
            case Bottom:
                position.set(windowX + graphBoxConnector.getOffset(), windowY - CONNECTOR_LENGTH);
                break;
        }
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
