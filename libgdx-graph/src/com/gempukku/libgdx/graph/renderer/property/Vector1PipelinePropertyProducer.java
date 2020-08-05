package com.gempukku.libgdx.graph.renderer.property;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class Vector1PipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Vector1");
    }

    @Override
    public WritablePipelineProperty createProperty(JSONObject data) {
        final float x = ((Number) data.get("x")).floatValue();
        return new WritablePipelineProperty(PropertyType.Vector1,
                new Supplier<Float>() {
                    @Override
                    public Float get() {
                        return x;
                    }
                });
    }
}
