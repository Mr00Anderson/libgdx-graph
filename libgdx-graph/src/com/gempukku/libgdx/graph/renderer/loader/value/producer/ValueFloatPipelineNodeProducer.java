package com.gempukku.libgdx.graph.renderer.loader.value.producer;

import com.gempukku.libgdx.graph.renderer.config.value.ValueFkiatPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.renderer.loader.value.node.ValuePipelineNode;
import org.json.simple.JSONObject;

import java.util.Map;

public class ValueFloatPipelineNodeProducer extends PipelineNodeProducerImpl {
    public ValueFloatPipelineNodeProducer() {
        super(new ValueFkiatPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new ValuePipelineNode(configuration, "value", ((Number) data.get("v1")).floatValue());
    }
}
