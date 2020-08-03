package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.graph.pipeline.producer.PipelineParticipantProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphContainer;
import org.json.simple.JSONObject;

public class UIPipelineLoaderCallback implements PipelineLoaderCallback {
    private Skin skin;
    private GraphContainer graphContainer;

    public UIPipelineLoaderCallback(Skin skin, GraphContainer graphContainer) {
        this.skin = skin;
        this.graphContainer = graphContainer;
    }

    @Override
    public void start() {

    }

    @Override
    public void addPipelineParticipant(PipelineParticipantProducer pipelineParticipantProducer, JSONObject object) {
        GraphBox graphBox = pipelineParticipantProducer.createPipelineGraphBox(skin, object);
        graphContainer.addGraphBox(graphBox);
    }

    @Override
    public void addPipelineParticipantConnection(String from, String to) {
        graphContainer.addGraphConnection(from, to);
    }

    @Override
    public void end() {

    }
}
