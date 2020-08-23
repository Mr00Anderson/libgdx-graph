package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.pipeline.GraphLoaderCallback;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphContainer;
import com.gempukku.libgdx.graph.ui.graph.GraphDesignTab;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBox;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.google.common.base.Predicate;
import org.json.simple.JSONObject;

import java.util.Map;
import java.util.Set;

public class UIGraphLoaderCallback<T extends FieldType> implements GraphLoaderCallback<GraphDesignTab<T>> {
    private Skin skin;
    private Map<String, PropertyBoxProducer<T>> propertyBoxProducers;
    private Map<String, Set<GraphBoxProducer<T>>> graphBoxProducers;
    private Predicate<GraphBoxProducer<T>> addablePredicate;

    private GraphDesignTab<T> graphDesignTab;
    private GraphContainer<T> graphContainer;

    public UIGraphLoaderCallback(Skin skin,
                                 Map<String, PropertyBoxProducer<T>> propertyBoxProducers,
                                 Map<String, Set<GraphBoxProducer<T>>> graphBoxProducers,
                                 Predicate<GraphBoxProducer<T>> addablePredicate) {
        this.skin = skin;
        this.propertyBoxProducers = propertyBoxProducers;
        this.graphBoxProducers = graphBoxProducers;
        this.addablePredicate = addablePredicate;
    }

    @Override
    public void start() {
        graphDesignTab = new GraphDesignTab<T>("main", "Render Pipeline", skin,
                propertyBoxProducers, graphBoxProducers, addablePredicate);
        graphContainer = graphDesignTab.getGraphContainer();
    }

    @Override
    public void addPipelineNode(String id, String type, float x, float y, JSONObject data) {
        GraphBoxProducer<T> producer = findProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find pipeline producer for type: " + type);
        GraphBox<T> graphBox = producer.createPipelineGraphBox(skin, id, data);
        graphContainer.addGraphBox(graphBox, producer.getTitle(), producer.isCloseable(), x, y);
    }

    @Override
    public void addPipelineVertex(String fromNode, String fromProperty, String toNode, String toProperty) {
        graphContainer.addGraphConnection(fromNode, fromProperty, toNode, toProperty);
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
        graphContainer.adjustCanvas();
        return graphDesignTab;
    }

    private PropertyBoxProducer<T> findPropertyProducerByType(String type) {
        for (PropertyBoxProducer<T> producer : propertyBoxProducers.values()) {
            if (producer.supportsType(type))
                return producer;
        }
        return null;
    }

    private GraphBoxProducer<T> findProducerByType(String type) {
        for (Set<GraphBoxProducer<T>> producers : graphBoxProducers.values()) {
            for (GraphBoxProducer<T> producer : producers) {
                if (producer.getType().equals(type))
                    return producer;
            }
        }

        return null;
    }
}
