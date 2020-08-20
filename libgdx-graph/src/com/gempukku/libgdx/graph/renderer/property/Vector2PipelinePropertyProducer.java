package com.gempukku.libgdx.graph.renderer.property;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class Vector2PipelinePropertyProducer implements PipelinePropertyProducer<PipelineFieldType> {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Vector2");
    }

    @Override
    public WritablePipelineProperty<PipelineFieldType> createProperty(JSONObject data) {
        final float x = ((Number) data.get("x")).floatValue();
        final float y = ((Number) data.get("y")).floatValue();
        return new WritablePipelineProperty<PipelineFieldType>(PipelineFieldType.Vector2,
                new Supplier<Vector2>() {
                    @Override
                    public Vector2 get() {
                        return new Vector2(x, y);
                    }
                });
    }
}
