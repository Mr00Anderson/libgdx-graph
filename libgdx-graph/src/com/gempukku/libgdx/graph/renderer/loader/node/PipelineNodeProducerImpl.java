package com.gempukku.libgdx.graph.renderer.loader.node;

public abstract class PipelineNodeProducerImpl implements PipelineNodeProducer {
    private PipelineNodeConfiguration configuration;

    public PipelineNodeProducerImpl(PipelineNodeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public final boolean supportsType(String type) {
        return type.equals(configuration.getType());
    }

    @Override
    public final PipelineNodeConfiguration getConfiguration() {
        return configuration;
    }
}
