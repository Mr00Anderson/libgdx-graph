package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInput;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutput;
import org.json.simple.JSONObject;

public class GraphBoxPartImpl implements GraphBoxPart {
    private Actor actor;
    private GraphBoxInputConnector inputConnector;
    private GraphBoxOutputConnector outputConnector;
    private Callback callback;

    public GraphBoxPartImpl(Actor actor, Callback callback) {
        this.actor = actor;
        this.callback = callback;
    }

    public void setInputConnector(GraphBoxInputConnector.Side side, PipelineNodeInput pipelineNodeInput) {
        inputConnector = new GraphBoxInputConnectorImpl(side, null, pipelineNodeInput);
    }

    public void setOutputConnector(GraphBoxOutputConnector.Side side, PipelineNodeOutput pipelineNodeOutput) {
        outputConnector = new GraphBoxOutputConnectorImpl(side, null, pipelineNodeOutput);
    }

    @Override
    public Actor getActor() {
        return actor;
    }

    @Override
    public GraphBoxInputConnector getInputConnector() {
        return inputConnector;
    }

    @Override
    public GraphBoxOutputConnector getOutputConnector() {
        return outputConnector;
    }

    @Override
    public void serializePart(JSONObject object) {
        if (callback != null)
            callback.serialize(object);
    }

    public interface Callback {
        void serialize(JSONObject object);
    }
}
