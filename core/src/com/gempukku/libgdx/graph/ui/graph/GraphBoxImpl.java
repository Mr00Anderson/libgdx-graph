package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.graph.renderer.PropertyType;
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
    private Table table;
    private List<GraphBoxPart> graphBoxParts = new LinkedList<>();
    private Map<String, GraphBoxInputConnector> inputConnectors = new HashMap<>();
    private Map<String, GraphBoxOutputConnector> outputConnectors = new HashMap<>();

    public GraphBoxImpl(String id, String type, Skin skin) {
        this.id = id;
        this.type = type;
        table = new Table(skin);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    public void addTopConnector(String id, Predicate<PropertyType> propertyTypePredicate) {
        inputConnectors.put(id, new GraphBoxInputConnectorImpl(id, GraphBoxInputConnector.Side.Top,
                propertyTypePredicate, new Supplier<Float>() {
            @Override
            public Float get() {
                return table.getWidth() / 2f;
            }
        }));
    }

    public void addBottomConnector(String id, Predicate<PropertyType> possiblePropertyTypes) {
        outputConnectors.put(id, new GraphBoxOutputConnectorImpl(id, GraphBoxOutputConnector.Side.Bottom,
                possiblePropertyTypes, new Supplier<Float>() {
            @Override
            public Float get() {
                return table.getWidth() / 2f;
            }
        }, false));
    }

    public void addTwoSideGraphPart(Skin skin,
                                    String inputId, String inputText, Predicate<PropertyType> inputType,
                                    String outputId, String outputText, Predicate<PropertyType> outputType) {
        Table table = new Table();
        table.add(new Label(inputText, skin)).grow();
        Label outputLabel = new Label(outputText, skin);
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow();
        table.row();

        GraphBoxPartImpl graphBoxPart = new GraphBoxPartImpl(table, null);
        graphBoxPart.setInputConnector(inputId, GraphBoxInputConnector.Side.Left, inputType);
        graphBoxPart.setOutputConnector(outputId, GraphBoxOutputConnector.Side.Right, outputType);
        addGraphBoxPart(graphBoxPart);
    }

    public void addInputGraphPart(Skin skin,
                                  String inputId, String inputText, Predicate<PropertyType> inputType) {
        Table table = new Table();
        table.add(new Label(inputText, skin)).grow().row();

        GraphBoxPartImpl graphBoxPart = new GraphBoxPartImpl(table, null);
        graphBoxPart.setInputConnector(inputId, GraphBoxInputConnector.Side.Left, inputType);
        addGraphBoxPart(graphBoxPart);
    }

    public void addOutputGraphPart(
            Skin skin,
            String id,
            String text,
            Predicate<PropertyType> type) {
        Table table = new Table();
        Label outputLabel = new Label(text, skin);
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow().row();

        GraphBoxPartImpl graphBoxPart = new GraphBoxPartImpl(table, null);
        graphBoxPart.setOutputConnector(id, GraphBoxOutputConnector.Side.Right, type);
        addGraphBoxPart(graphBoxPart);
    }

    public void addGraphBoxPart(GraphBoxPart graphBoxPart) {
        graphBoxParts.add(graphBoxPart);
        final Actor actor = graphBoxPart.getActor();
        table.add(actor).growX().row();
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
        final GraphBoxOutputConnector outputConnector = graphBoxPart.getOutputConnector();
        if (outputConnector != null) {
            String id = outputConnector.getId();
            outputConnectors.put(id + ":" + id,
                    new GraphBoxOutputConnectorImpl(id, outputConnector.getSide(), new Predicate<PropertyType>() {
                        @Override
                        public boolean apply(@Nullable PropertyType propertyType) {
                            return outputConnector.produces(propertyType);
                        }
                    },
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
    public Actor getActor() {
        return table;
    }

    @Override
    public JSONObject serializeData() {
        JSONObject result = new JSONObject();

        for (GraphBoxPart graphBoxPart : graphBoxParts)
            graphBoxPart.serializePart(result);

        if (result.isEmpty())
            return null;
        return result;
    }
}
