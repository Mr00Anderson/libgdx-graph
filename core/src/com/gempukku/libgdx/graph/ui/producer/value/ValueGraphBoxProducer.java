package com.gempukku.libgdx.graph.ui.producer.value;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;

public abstract class ValueGraphBoxProducer<T extends FieldType> implements GraphBoxProducer<T> {
    protected PipelineNodeConfiguration<T> configuration;

    public ValueGraphBoxProducer(PipelineNodeConfiguration<T> configuration) {
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
    public final String getTitle() {
        return configuration.getName();
    }
}
