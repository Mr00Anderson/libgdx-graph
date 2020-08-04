package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.PipelineConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphContainer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import org.json.simple.JSONObject;

import java.util.Map;

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
    public void addPipelineParticipant(String id, String type, float x, float y, JSONObject data) {
        GraphBoxProducer producer = findProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find pipeline producer for type: " + type);
        GraphBox graphBox = producer.createPipelineGraphBox(skin, id, data);
        graphContainer.addGraphBox(graphBox, producer.getTitle(), producer.isCloseable(), x, y);
    }

    @Override
    public void addPipelineParticipantConnection(String from, String to) {
        graphContainer.addGraphConnection(from, to);
    }

    @Override
    public void addPipelineProperty(String type, JSONObject data) {

    }

    @Override
    public void end() {

    }

    private static GraphBoxProducer findProducerByType(String type) {
        for (Map<String, GraphBoxProducer> producers : PipelineConfiguration.graphBoxProducers.values()) {
            for (GraphBoxProducer producer : producers.values()) {
                if (producer.supportsType(type))
                    return producer;
            }
        }
        return null;
    }
}
