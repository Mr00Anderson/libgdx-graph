package com.gempukku.libgdx.graph.renderer.property;

import com.badlogic.gdx.graphics.Camera;
import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class CameraPipelinePropertyProducer implements PipelinePropertyProducer<PipelineFieldType> {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Camera");
    }

    @Override
    public WritablePipelineProperty<PipelineFieldType> createProperty(JSONObject data) {
        return new WritablePipelineProperty<PipelineFieldType>(PipelineFieldType.Camera,
                new Supplier<Camera>() {
                    @Override
                    public Camera get() {
                        return null;
                    }
                });
    }
}
