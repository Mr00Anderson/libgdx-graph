package com.gempukku.libgdx.graph.renderer.property;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.renderer.impl.WritablePipelineProperty;
import org.json.simple.JSONObject;

public interface PipelinePropertyProducer<T extends FieldType> {
    boolean supportsType(String type);

    WritablePipelineProperty<T> createProperty(JSONObject data);
}
