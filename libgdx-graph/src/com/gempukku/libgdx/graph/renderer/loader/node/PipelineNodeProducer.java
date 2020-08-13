package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import java.util.Map;

public interface PipelineNodeProducer {
    String getType();

    PipelineNodeConfiguration getConfiguration(JSONObject data);

    PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions);
}
