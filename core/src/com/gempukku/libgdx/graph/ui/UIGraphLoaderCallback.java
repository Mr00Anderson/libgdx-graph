package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.GraphLoaderCallback;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphDesignTab;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import org.json.simple.JSONObject;

import java.util.Set;

public class UIGraphLoaderCallback<T extends FieldType> implements GraphLoaderCallback<GraphDesignTab<T>> {
    private Skin skin;
    private GraphDesignTab<T> graphDesignTab;
    private UIGraphConfiguration<T> uiGraphConfiguration;

    public UIGraphLoaderCallback(Skin skin, GraphDesignTab<T> graphDesignTab, UIGraphConfiguration<T> uiGraphConfiguration) {
        this.skin = skin;
        this.graphDesignTab = graphDesignTab;
        this.uiGraphConfiguration = uiGraphConfiguration;
    }

    @Override
    public void start() {
    }

    @Override
    public void addPipelineNode(String id, String type, float x, float y, JSONObject data) {
        GraphBoxProducer<T> producer = findProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find pipeline producer for type: " + type);
        GraphBox<T> graphBox = producer.createPipelineGraphBox(skin, id, data);
        graphDesignTab.getGraphContainer().addGraphBox(graphBox, producer.getTitle(), producer.isCloseable(), x, y);
    }

    @Override
    public void addPipelineVertex(String fromNode, String fromProperty, String toNode, String toProperty) {
        graphDesignTab.getGraphContainer().addGraphConnection(fromNode, fromProperty, toNode, toProperty);
    }

    @Override
    public void addPipelineProperty(String type, String name, JSONObject data) {
        PropertyBoxProducer<T> producer = findPropertyProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find property producer for type: " + type);
        PropertyBox<T> propertyBox = producer.createPropertyBox(skin, name, data);
        graphDesignTab.addPropertyBox(skin, type, propertyBox);
    }

    @Override
    public GraphDesignTab<T> end() {
        graphDesignTab.getGraphContainer().adjustCanvas();
        return graphDesignTab;
    }

    private PropertyBoxProducer<T> findPropertyProducerByType(String type) {
        for (PropertyBoxProducer<T> producer : uiGraphConfiguration.getPropertyBoxProducers().values()) {
            if (producer.getType().name().equals(type))
                return producer;
        }
        return null;
    }

    private GraphBoxProducer<T> findProducerByType(String type) {
        for (Set<GraphBoxProducer<T>> producers : uiGraphConfiguration.getGraphBoxProducers().values()) {
            for (GraphBoxProducer<T> producer : producers) {
                if (producer.getType().equals(type))
                    return producer;
            }
        }

        return null;
    }
}
