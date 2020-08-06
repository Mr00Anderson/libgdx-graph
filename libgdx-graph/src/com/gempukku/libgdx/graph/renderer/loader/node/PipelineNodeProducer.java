package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import java.util.Map;

public interface PipelineNodeProducer {
    boolean supportsType(String type);

    PipelineNodeConfiguration getConfiguration();

    PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions);
}
