package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInput;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutput;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphBoxImpl implements GraphBox {
    private String id;
    private PipelineNodeConfiguration configuration;
    private Table table;
    private List<GraphBoxPart> graphBoxParts = new LinkedList<>();
    private Map<String, GraphBoxInputConnector> inputConnectors = new HashMap<>();
    private Map<String, GraphBoxOutputConnector> outputConnectors = new HashMap<>();

    public GraphBoxImpl(String id, PipelineNodeConfiguration configuration, Skin skin) {
        this.id = id;
        this.configuration = configuration;
        table = new Table(skin);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public GraphBoxInputConnector getInput(String fieldId) {
        return inputConnectors.get(fieldId);
    }

    @Override
    public GraphBoxOutputConnector getOutput(String fieldId) {
        return outputConnectors.get(fieldId);
    }

    @Override
    public boolean isInputField(String fieldId) {
        return inputConnectors.containsKey(fieldId);
    }

    public void addTopConnector(PipelineNodeInput pipelineNodeInput) {
        inputConnectors.put(pipelineNodeInput.getFieldId(), new GraphBoxInputConnectorImpl(GraphBoxInputConnector.Side.Top, new Supplier<Float>() {
            @Override
            public Float get() {
                return table.getWidth() / 2f;
            }
        }, pipelineNodeInput));
    }

    public void addBottomConnector(PipelineNodeOutput pipelineNodeOutput) {
        outputConnectors.put(pipelineNodeOutput.getFieldId(), new GraphBoxOutputConnectorImpl(GraphBoxOutputConnector.Side.Bottom,
                new Supplier<Float>() {
                    @Override
                    public Float get() {
                        return table.getWidth() / 2f;
                    }
                }, pipelineNodeOutput));
    }

    public void addTwoSideGraphPart(Skin skin,
                                    PipelineNodeInput pipelineNodeInput,
                                    PipelineNodeOutput pipelineNodeOutput) {
        Table table = new Table();
        table.add(new Label(pipelineNodeInput.getFieldName(), skin)).grow();
        Label outputLabel = new Label(pipelineNodeOutput.getFieldName(), skin);
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow();
        table.row();

        GraphBoxPartImpl graphBoxPart = new GraphBoxPartImpl(table, null);
        graphBoxPart.setInputConnector(GraphBoxInputConnector.Side.Left, pipelineNodeInput);
        graphBoxPart.setOutputConnector(GraphBoxOutputConnector.Side.Right, pipelineNodeOutput);
        addGraphBoxPart(graphBoxPart);
    }

    public void addInputGraphPart(Skin skin,
                                  PipelineNodeInput pipelineNodeInput) {
        Table table = new Table();
        table.add(new Label(pipelineNodeInput.getFieldName(), skin)).grow().row();

        GraphBoxPartImpl graphBoxPart = new GraphBoxPartImpl(table, null);
        graphBoxPart.setInputConnector(GraphBoxInputConnector.Side.Left, pipelineNodeInput);
        addGraphBoxPart(graphBoxPart);
    }

    public void addOutputGraphPart(
            Skin skin,
            PipelineNodeOutput pipelineNodeOutput) {
        Table table = new Table();
        Label outputLabel = new Label(pipelineNodeOutput.getFieldName(), skin);
        outputLabel.setAlignment(Align.right);
        table.add(outputLabel).grow().row();

        GraphBoxPartImpl graphBoxPart = new GraphBoxPartImpl(table, null);
        graphBoxPart.setOutputConnector(GraphBoxOutputConnector.Side.Right, pipelineNodeOutput);
        addGraphBoxPart(graphBoxPart);
    }

    public void addGraphBoxPart(GraphBoxPart graphBoxPart) {
        graphBoxParts.add(graphBoxPart);
        final Actor actor = graphBoxPart.getActor();
        table.add(actor).growX().row();
        final GraphBoxInputConnector inputConnector = graphBoxPart.getInputConnector();
        if (inputConnector != null) {
            inputConnectors.put(inputConnector.getFieldId(),
                    new GraphBoxInputConnectorImpl(inputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            inputConnector));
        }
        final GraphBoxOutputConnector outputConnector = graphBoxPart.getOutputConnector();
        if (outputConnector != null) {
            outputConnectors.put(outputConnector.getFieldId(),
                    new GraphBoxOutputConnectorImpl(outputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            outputConnector));
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
