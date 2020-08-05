package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphContainer;
import com.gempukku.libgdx.graph.ui.pipeline.PipelineDesignTab;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBox;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import org.json.simple.JSONObject;

import java.util.Map;

public class UIPipelineLoaderCallback implements PipelineLoaderCallback<PipelineDesignTab> {
    private Skin skin;
    private PipelineDesignTab pipelineDesignTab;
    private GraphContainer graphContainer;

    public UIPipelineLoaderCallback(Skin skin) {
        this.skin = skin;
    }

    @Override
    public void start() {
        pipelineDesignTab = new PipelineDesignTab(skin);
        graphContainer = pipelineDesignTab.getGraphContainer();
    }

    @Override
    public void addPipelineNode(String id, String type, float x, float y, JSONObject data) {
        GraphBoxProducer producer = findProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find pipeline producer for type: " + type);
        GraphBox graphBox = producer.createPipelineGraphBox(skin, id, data);
        graphContainer.addGraphBox(graphBox, producer.getTitle(), producer.isCloseable(), x, y);
    }

    @Override
    public void addPipelineVertex(String fromNode, String fromProperty, String toNode, String toProperty) {
        graphContainer.addGraphConnection(fromNode + ":" + fromProperty, toNode + ":" + toProperty);
    }

    @Override
    public void addPipelineProperty(String type, String name, JSONObject data) {
        PropertyBoxProducer producer = findPropertyProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find property producer for type: " + type);
        PropertyBox propertyBox = producer.createPropertyBox(skin, name, data);
        pipelineDesignTab.addPropertyBox(skin, type, propertyBox);
    }

    @Override
    public PipelineDesignTab end() {
        return pipelineDesignTab;
    }

    private static PropertyBoxProducer findPropertyProducerByType(String type) {
        for (PropertyBoxProducer producer : UIPipelineConfiguration.propertyProducers.values()) {
            if (producer.supportsType(type))
                return producer;
        }

        return null;
    }

    private static GraphBoxProducer findProducerByType(String type) {
        for (Map<String, GraphBoxProducer> producers : UIPipelineConfiguration.graphBoxProducers.values()) {
            for (GraphBoxProducer producer : producers.values()) {
                if (producer.supportsType(type))
                    return producer;
            }
        }
        return null;
    }
}
