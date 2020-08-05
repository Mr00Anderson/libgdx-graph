package com.gempukku.libgdx.graph.renderer.property;

import com.gempukku.libgdx.graph.renderer.impl.WritablePipelineProperty;
import org.json.simple.JSONObject;

public interface PipelinePropertyProducer {
    boolean supportsType(String type);

    WritablePipelineProperty createProperty(JSONObject data);
}
