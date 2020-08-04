package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
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
    public void addPipelineParticipant(GraphBoxProducer pipelineParticipantProducer, String id, float x, float y, JSONObject data) {
        GraphBox graphBox = pipelineParticipantProducer.createPipelineGraphBox(skin, id, data);
        graphContainer.addGraphBox(graphBox, pipelineParticipantProducer.getTitle(), pipelineParticipantProducer.isCloseable(), x, y);
    }

    @Override
    public void addPipelineParticipantConnection(String from, String to) {
        graphContainer.addGraphConnection(from, to);
    }

    @Override
    public void end() {

    }
}
