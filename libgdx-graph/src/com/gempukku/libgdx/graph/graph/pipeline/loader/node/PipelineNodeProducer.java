package com.gempukku.libgdx.graph.graph.pipeline.loader.node;

import com.gempukku.libgdx.graph.graph.NodeConfiguration;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import org.json.simple.JSONObject;

import java.util.Map;

public interface PipelineNodeProducer {
    String getType();

    NodeConfiguration<PipelineFieldType> getConfiguration(JSONObject data);

    PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields);
}
