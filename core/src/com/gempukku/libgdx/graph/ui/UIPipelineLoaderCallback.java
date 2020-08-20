package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphContainer;
import com.gempukku.libgdx.graph.ui.pipeline.PipelineDesignTab;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBox;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import org.json.simple.JSONObject;

import java.util.Set;

public class UIPipelineLoaderCallback implements PipelineLoaderCallback<PipelineDesignTab> {
    private Skin skin;
    private PipelineDesignTab pipelineDesignTab;
    private GraphContainer<PipelineFieldType> graphContainer;

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
        GraphBoxProducer<PipelineFieldType> producer = findProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find pipeline producer for type: " + type);
        GraphBox<PipelineFieldType> graphBox = producer.createPipelineGraphBox(skin, id, data);
        graphContainer.addGraphBox(graphBox, producer.getTitle(), producer.isCloseable(), x, y);
    }

    @Override
    public void addPipelineVertex(String fromNode, String fromProperty, String toNode, String toProperty) {
        graphContainer.addGraphConnection(fromNode, fromProperty, toNode, toProperty);
    }

    @Override
    public void addPipelineProperty(String type, String name, JSONObject data) {
        PropertyBoxProducer<PipelineFieldType> producer = findPropertyProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find property producer for type: " + type);
        PropertyBox<PipelineFieldType> propertyBox = producer.createPropertyBox(skin, name, data);
        pipelineDesignTab.addPropertyBox(skin, type, propertyBox);
    }

    @Override
    public PipelineDesignTab end() {
        graphContainer.adjustCanvas();
        return pipelineDesignTab;
    }

    private static PropertyBoxProducer<PipelineFieldType> findPropertyProducerByType(String type) {
        for (PropertyBoxProducer<PipelineFieldType> producer : UIPipelineConfiguration.propertyProducers.values()) {
            if (producer.supportsType(type))
                return producer;
        }

        return null;
    }

    private static GraphBoxProducer<PipelineFieldType> findProducerByType(String type) {
        for (Set<GraphBoxProducer<PipelineFieldType>> producers : UIPipelineConfiguration.graphBoxProducers.values()) {
            for (GraphBoxProducer<PipelineFieldType> producer : producers) {
                if (producer.getType().equals(type))
                    return producer;
            }
        }
        return null;
    }
}
