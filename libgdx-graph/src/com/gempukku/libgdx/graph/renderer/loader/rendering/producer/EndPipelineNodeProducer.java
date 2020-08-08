package com.gempukku.libgdx.graph.renderer.loader.rendering.producer;

import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.config.rendering.EndPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.renderer.loader.rendering.node.EndPipelineNode;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import java.util.Map;

public class EndPipelineNodeProducer extends PipelineNodeProducerImpl {
    public EndPipelineNodeProducer() {
        super(new EndPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        return new EndPipelineNode(
                (Function<PipelineRenderingContext, RenderPipeline>) inputFunctions.get("input"));
    }
}
