package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.gempukku.graph.pipeline.PropertyType;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphBoxImpl implements GraphBox {
    private String id;
    private String type;
    private Window window;
    private List<GraphBoxPart> graphBoxParts = new LinkedList<>();
    private Map<String, GraphBoxInputConnector> inputConnectors = new HashMap<>();
    private Map<String, GraphBoxOutputConnector> outputConnectors = new HashMap<>();

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
        inputConnectors.put(id, new GraphBoxInputConnectorImpl(id, GraphBoxInputConnector.Side.Top,
                new Predicate<PropertyType>() {
                    @Override
                    public boolean apply(@Nullable PropertyType propertyType) {
                        return propertyType == PropertyType.PipelineParticipant;
                    }
                }, new Supplier<Float>() {
            @Override
            public Float get() {
                return window.getWidth() / 2f;
            }
        }));
    }

    public void addBottomConnector(String id) {
        outputConnectors.put(id, new GraphBoxOutputConnectorImpl(id, GraphBoxOutputConnector.Side.Bottom,
                PropertyType.PipelineParticipant, new Supplier<Float>() {
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
        final GraphBoxInputConnector inputConnector = graphBoxPart.getInputConnector();
        if (inputConnector != null) {
            String id = inputConnector.getId();
            inputConnectors.put(id + ":" + id,
                    new GraphBoxInputConnectorImpl(id, inputConnector.getSide(),
                            new Predicate<PropertyType>() {
                                @Override
                                public boolean apply(@Nullable PropertyType propertyType) {
                                    return inputConnector.accepts(propertyType);
                                }
                            }, new Supplier<Float>() {
                        @Override
                        public Float get() {
                            return actor.getY() + actor.getHeight() / 2f;
                        }
                    }));
        }
        GraphBoxOutputConnector outputConnector = graphBoxPart.getOutputConnector();
        if (outputConnector != null) {
            String id = outputConnector.getId();
            outputConnectors.put(id + ":" + id,
                    new GraphBoxOutputConnectorImpl(id, outputConnector.getSide(), outputConnector.getPropertyType(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            }));
        }
    }

    @Override
    public Iterable<GraphBoxInputConnector> getGraphBoxInputConnectors() {
        return inputConnectors.values();
    }

    @Override
    public Iterable<GraphBoxOutputConnector> getGraphBoxOutputConnectors() {
        return outputConnectors.values();
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
