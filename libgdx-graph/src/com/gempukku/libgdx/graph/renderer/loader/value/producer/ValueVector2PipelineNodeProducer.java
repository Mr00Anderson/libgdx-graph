package com.gempukku.libgdx.graph.renderer.loader.value.producer;

import com.badlogic.gdx.math.Vector2;
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

public class ValueVector2PipelineNodeProducer implements PipelineNodeProducer {
    private PipelineNodeConfigurationImpl configuration;

    public ValueVector2PipelineNodeProducer() {
        configuration = new PipelineNodeConfigurationImpl();
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("value", null));
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("ValueVector2");
    }

    @Override
    public PipelineNodeConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputSuppliers) {
        return new ValuePipelineNode("value", new Vector2(
                ((Number) data.get("v1")).floatValue(),
                ((Number) data.get("v2")).floatValue()));
    }
}
