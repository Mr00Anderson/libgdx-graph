package com.gempukku.libgdx.graph.pipeline.property;

import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class FloatPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Float;
    }

    @Override
    public WritablePipelineProperty createProperty(JSONObject data) {
        final float x = ((Number) data.get("x")).floatValue();
        return new WritablePipelineProperty(PipelineFieldType.Float,
                new Supplier<Float>() {
                    @Override
                    public Float get() {
                        return x;
                    }
                });
    }
}
