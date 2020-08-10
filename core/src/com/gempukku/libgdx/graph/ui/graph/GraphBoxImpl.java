package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInput;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutput;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class GraphBoxImpl implements GraphBox {
    private String id;
    private String type;
    private Table table;
    private List<GraphBoxPart> graphBoxParts = new LinkedList<>();
    private List<GraphBoxInputConnector> inputConnectors = new LinkedList<>();
    private List<GraphBoxOutputConnector> outputConnectors = new LinkedList<>();

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

    public void addTopConnector(PipelineNodeInput pipelineNodeInput) {
        inputConnectors.add(new GraphBoxInputConnectorImpl(GraphBoxInputConnector.Side.Top, new Supplier<Float>() {
            @Override
            public Float get() {
                return table.getWidth() / 2f;
            }
        }, pipelineNodeInput));
    }

    public void addBottomConnector(PipelineNodeOutput pipelineNodeOutput) {
        outputConnectors.add(new GraphBoxOutputConnectorImpl(GraphBoxOutputConnector.Side.Bottom,
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
            inputConnectors.add(
                    new GraphBoxInputConnectorImpl(inputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            inputConnector.getPipelineNodeInput()));
        }
        final GraphBoxOutputConnector outputConnector = graphBoxPart.getOutputConnector();
        if (outputConnector != null) {
            outputConnectors.add(
                    new GraphBoxOutputConnectorImpl(outputConnector.getSide(),
                            new Supplier<Float>() {
                                @Override
                                public Float get() {
                                    return actor.getY() + actor.getHeight() / 2f;
                                }
                            },
                            outputConnector.getPipelineNodeOutput()));
        }
    }

    @Override
    public Iterable<GraphBoxInputConnector> getGraphBoxInputConnectors() {
        return inputConnectors;
    }

    @Override
    public Iterable<GraphBoxOutputConnector> getGraphBoxOutputConnectors() {
        return outputConnectors;
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
