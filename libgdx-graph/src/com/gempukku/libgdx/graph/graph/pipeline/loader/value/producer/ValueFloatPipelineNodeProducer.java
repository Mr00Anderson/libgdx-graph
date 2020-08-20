package com.gempukku.libgdx.graph.graph.pipeline.loader.value.producer;

import com.gempukku.libgdx.graph.graph.pipeline.config.value.ValueFloatPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.graph.pipeline.loader.value.node.ValuePipelineNode;
import org.json.simple.JSONObject;

import java.util.Map;

public class ValueFloatPipelineNodeProducer extends PipelineNodeProducerImpl {
    public ValueFloatPipelineNodeProducer() {
        super(new ValueFloatPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new ValuePipelineNode(configuration, "value", ((Number) data.get("v1")).floatValue());
    }
}
