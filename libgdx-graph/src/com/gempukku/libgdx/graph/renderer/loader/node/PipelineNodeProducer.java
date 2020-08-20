package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import org.json.simple.JSONObject;

import java.util.Map;

public interface PipelineNodeProducer {
    String getType();

    PipelineNodeConfiguration<PipelineFieldType> getConfiguration(JSONObject data);

    PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields);
}
