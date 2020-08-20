package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.gempukku.libgdx.graph.pipeline.config.rendering.StartPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.node.StartPipelineNode;
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
