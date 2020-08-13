package com.gempukku.libgdx.graph.renderer.loader.rendering.producer;

import com.gempukku.libgdx.graph.renderer.config.rendering.StartPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.renderer.loader.rendering.node.StartPipelineNode;
import org.json.simple.JSONObject;

import java.util.Map;

public class StartPipelineNodeProducer extends PipelineNodeProducerImpl {
    public StartPipelineNodeProducer() {
        super(new StartPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new StartPipelineNode(configuration, inputFields);
    }
}
