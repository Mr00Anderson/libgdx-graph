package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import org.json.simple.JSONObject;

public abstract class PipelineNodeProducerImpl implements PipelineNodeProducer {
    protected PipelineNodeConfiguration<PipelineFieldType> configuration;

    public PipelineNodeProducerImpl(PipelineNodeConfiguration<PipelineFieldType> configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public final PipelineNodeConfiguration<PipelineFieldType> getConfiguration(JSONObject data) {
        return configuration;
    }
}
