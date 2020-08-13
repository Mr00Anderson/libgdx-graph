package com.gempukku.libgdx.graph.renderer.loader.node;

import org.json.simple.JSONObject;

public abstract class PipelineNodeProducerImpl implements PipelineNodeProducer {
    protected PipelineNodeConfiguration configuration;

    public PipelineNodeProducerImpl(PipelineNodeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public final PipelineNodeConfiguration getConfiguration(JSONObject data) {
        return configuration;
    }
}
