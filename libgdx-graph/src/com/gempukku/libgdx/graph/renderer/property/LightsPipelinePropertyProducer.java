package com.gempukku.libgdx.graph.renderer.property;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class LightsPipelinePropertyProducer implements PipelinePropertyProducer<PipelineFieldType> {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Lights");
    }

    @Override
    public WritablePipelineProperty<PipelineFieldType> createProperty(JSONObject data) {
        return new WritablePipelineProperty<PipelineFieldType>(PipelineFieldType.Lights,
                new Supplier<Environment>() {
                    @Override
                    public Environment get() {
                        return null;
                    }
                });
    }
}
