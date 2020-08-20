package com.gempukku.libgdx.graph.renderer.property;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class FloatPipelinePropertyProducer implements PipelinePropertyProducer<PipelineFieldType> {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Float");
    }

    @Override
    public WritablePipelineProperty<PipelineFieldType> createProperty(JSONObject data) {
        final float x = ((Number) data.get("x")).floatValue();
        return new WritablePipelineProperty<PipelineFieldType>(PipelineFieldType.Float,
                new Supplier<Float>() {
                    @Override
                    public Float get() {
                        return x;
                    }
                });
    }
}
