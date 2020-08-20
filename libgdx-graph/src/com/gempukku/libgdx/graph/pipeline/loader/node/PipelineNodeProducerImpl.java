package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.gempukku.libgdx.graph.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import org.json.simple.JSONObject;

public abstract class PipelineNodeProducerImpl implements PipelineNodeProducer {
    protected NodeConfiguration<PipelineFieldType> configuration;

    public PipelineNodeProducerImpl(NodeConfiguration<PipelineFieldType> configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public final NodeConfiguration<PipelineFieldType> getConfiguration(JSONObject data) {
        return configuration;
    }
}
