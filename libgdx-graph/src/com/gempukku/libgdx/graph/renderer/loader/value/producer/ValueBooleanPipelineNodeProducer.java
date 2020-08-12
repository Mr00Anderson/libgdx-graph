package com.gempukku.libgdx.graph.renderer.loader.value.producer;

import com.gempukku.libgdx.graph.renderer.config.value.ValueBooleanPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.renderer.loader.value.node.ValuePipelineNode;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import java.util.Map;

public class ValueBooleanPipelineNodeProducer extends PipelineNodeProducerImpl {
    public ValueBooleanPipelineNodeProducer() {
        super(new ValueBooleanPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputSuppliers) {
        return new ValuePipelineNode(getConfiguration(), "value", (Boolean) data.get("value"));
    }
}
