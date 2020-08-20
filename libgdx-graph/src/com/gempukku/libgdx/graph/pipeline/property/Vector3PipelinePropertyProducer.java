package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class Vector3PipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Vector3");
    }

    @Override
    public WritablePipelineProperty createProperty(JSONObject data) {
        final float x = ((Number) data.get("x")).floatValue();
        final float y = ((Number) data.get("y")).floatValue();
        final float z = ((Number) data.get("z")).floatValue();
        return new WritablePipelineProperty(PipelineFieldType.Vector3,
                new Supplier<Vector3>() {
                    @Override
                    public Vector3 get() {
                        return new Vector3(x, y, z);
                    }
                });
    }
}
