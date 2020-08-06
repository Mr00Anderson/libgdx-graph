package com.gempukku.libgdx.graph.renderer.loader.rendering.producer;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.node.EndPipelineNode;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
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
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        return new EndPipelineNode(
                (Function<PipelineRenderingContext, RenderPipeline>) inputFunctions.get("input"));
    }
}
