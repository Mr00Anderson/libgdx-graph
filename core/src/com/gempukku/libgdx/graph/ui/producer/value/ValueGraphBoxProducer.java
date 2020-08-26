package com.gempukku.libgdx.graph.ui.producer.value;

import com.gempukku.libgdx.graph.NodeConfiguration;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;

public abstract class ValueGraphBoxProducer<T extends FieldType> implements GraphBoxProducer<T> {
    protected NodeConfiguration<T> configuration;

    public ValueGraphBoxProducer(NodeConfiguration<T> configuration) {
        this.configuration = configuration;
    }

    @Override
    public final String getType() {
        return configuration.getType();
    }

    @Override
    public final boolean isCloseable() {
        return true;
    }

    @Override
    public final String getName() {
        return configuration.getName();
    }

    @Override
    public String getMenuLocation() {
        return configuration.getMenuLocation();
    }
}
