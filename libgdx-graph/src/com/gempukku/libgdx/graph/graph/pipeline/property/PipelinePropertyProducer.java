package com.gempukku.libgdx.graph.graph.pipeline.property;

import com.gempukku.libgdx.graph.graph.pipeline.impl.WritablePipelineProperty;
import org.json.simple.JSONObject;

public interface PipelinePropertyProducer {
    boolean supportsType(String type);

    WritablePipelineProperty createProperty(JSONObject data);
}
