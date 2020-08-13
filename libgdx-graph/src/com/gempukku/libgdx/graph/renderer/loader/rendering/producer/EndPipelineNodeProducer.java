package com.gempukku.libgdx.graph.renderer.loader.rendering.producer;

import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.config.rendering.EndPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.renderer.loader.rendering.node.EndPipelineNode;
import org.json.simple.JSONObject;

import java.util.Map;

public class EndPipelineNodeProducer extends PipelineNodeProducerImpl {
    public EndPipelineNodeProducer() {
        super(new EndPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new EndPipelineNode(
                (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input"));
    }
}
