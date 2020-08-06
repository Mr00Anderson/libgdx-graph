package com.gempukku.libgdx.graph.renderer.loader.value.producer;

import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.value.node.ValuePipelineNode;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import java.util.Map;

public class ValueVector1PipelineNodeProducer implements PipelineNodeProducer {
    private PipelineNodeConfigurationImpl configuration;

    public ValueVector1PipelineNodeProducer() {
        configuration = new PipelineNodeConfigurationImpl();
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("value", null));
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("ValueVector1");
    }

    @Override
    public PipelineNodeConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputSuppliers) {
        return new ValuePipelineNode("value", ((Number) data.get("v1")).floatValue());
    }
}
