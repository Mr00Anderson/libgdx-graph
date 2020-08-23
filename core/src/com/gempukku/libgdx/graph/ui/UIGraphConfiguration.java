package com.gempukku.libgdx.graph.ui;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;

import java.util.Map;
import java.util.Set;

public interface UIGraphConfiguration<T extends FieldType> {
    Map<String, Set<GraphBoxProducer<T>>> getGraphBoxProducers();

    Map<String, PropertyBoxProducer<T>> getPropertyBoxProducers();

    boolean isAddableGraphBox(GraphBoxProducer<T> graphBoxProducer);
}
