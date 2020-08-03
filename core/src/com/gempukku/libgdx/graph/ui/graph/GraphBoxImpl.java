package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.gempukku.graph.pipeline.PropertyType;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphBoxImpl implements GraphBox {
    private String id;
    private String type;
    private Window window;
    private List<GraphBoxPart> graphBoxParts = new LinkedList<>();
    private Map<String, GraphBoxConnector> boxConnectors = new HashMap<>();

    public GraphBoxImpl(String id, String type, String title, Skin skin) {
        this.id = id;
        this.type = type;
        window = new Window(title, skin) {
            @Override
            protected void positionChanged() {
                invalidateHierarchy();
            }
        };
        window.setKeepWithinStage(false);
    }

    public void addTopConnector(String id) {
        boxConnectors.put(id, new GraphBoxConnectorImpl(id, GraphBoxConnector.Side.Top,
                GraphBoxConnector.CommunicationType.Input, PropertyType.PipelineParticipant, new Supplier<Float>() {
            @Override
            public Float get() {
                return window.getWidth() / 2f;
            }
        }));
    }

    public void addBottomConnector(String id) {
        boxConnectors.put(id, new GraphBoxConnectorImpl(id, GraphBoxConnector.Side.Bottom,
                GraphBoxConnector.CommunicationType.Output, PropertyType.PipelineParticipant, new Supplier<Float>() {
            @Override
            public Float get() {
                return window.getWidth() / 2f;
            }
        }));
    }

    public void setPosition(float x, float y) {
        window.setPosition(x, y);
        window.invalidate();
    }

    public void addGraphBoxPart(GraphBoxPart graphBoxPart) {
        graphBoxParts.add(graphBoxPart);
        final Actor actor = graphBoxPart.getActor();
        window.add(actor).growX().row();
        for (GraphBoxConnector connector : graphBoxPart.getConnectors()) {
            String id = connector.getId();
            boxConnectors.put(id + ":" + id,
                    new GraphBoxConnectorImpl(id, connector.getSide(), connector.getCommunicationType(),
                            connector.getPropertyType(), new Supplier<Float>() {
                        @Override
                        public Float get() {
                            return actor.getY() + actor.getHeight() / 2f;
                        }
                    }));
        }
    }

    @Override
    public GraphBoxConnector getGraphBoxConnector(String id) {
        return boxConnectors.get(id);
    }

    @Override
    public Iterable<GraphBoxConnector> getGraphBoxConnectors() {
        return boxConnectors.values();
    }

    @Override
    public Window getWindow() {
        return window;
    }

    @Override
    public JSONObject serializeGraphBox() {
        JSONObject result = new JSONObject();
        result.put("id", id);
        result.put("type", type);
        result.put("x", window.getX());
        result.put("y", window.getY());

        for (GraphBoxPart graphBoxPart : graphBoxParts)
            graphBoxPart.serializePart(result);

        return result;
    }
}
