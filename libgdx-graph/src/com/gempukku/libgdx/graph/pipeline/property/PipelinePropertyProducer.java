package com.gempukku.libgdx.graph.pipeline.property;

import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import org.json.simple.JSONObject;

public interface PipelinePropertyProducer {
    PipelineFieldType getType();

    WritablePipelineProperty createProperty(JSONObject data);
}
