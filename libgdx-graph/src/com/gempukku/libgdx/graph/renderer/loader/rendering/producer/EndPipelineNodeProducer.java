package com.gempukku.libgdx.graph.renderer.loader.rendering.producer;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.node.EndPipelineNode;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

import java.util.Map;

public class EndPipelineNodeProducer implements PipelineNodeProducer {
    public static final String type = "PipelineEnd";
    private PipelineNodeConfigurationImpl configuration = new PipelineNodeConfigurationImpl();

    public EndPipelineNodeProducer() {
        configuration.addNodeInput(
                new PipelineNodeInputImpl(true, "input", Predicates.equalTo(PropertyType.RenderPipeline)));
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals(type);
    }

    @Override
    public PipelineNodeConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Supplier<?>> inputSuppliers) {
        return new EndPipelineNode(
                (Supplier<RenderPipeline>) inputSuppliers.get("input"));
    }
}
