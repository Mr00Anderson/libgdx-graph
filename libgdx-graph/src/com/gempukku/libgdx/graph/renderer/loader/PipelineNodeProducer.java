package com.gempukku.libgdx.graph.renderer.loader;

import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

import java.util.Map;

public interface PipelineNodeProducer {
    boolean supportsType(String type);

    PipelineNodeConfiguration getConfiguration();

    PipelineNode createNode(JSONObject data, Map<String, Supplier<?>> inputSuppliers);
}
