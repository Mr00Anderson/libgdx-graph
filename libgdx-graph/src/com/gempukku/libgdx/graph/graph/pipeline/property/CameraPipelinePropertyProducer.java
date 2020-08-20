package com.gempukku.libgdx.graph.graph.pipeline.property;

import com.badlogic.gdx.graphics.Camera;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class CameraPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Camera");
    }

    @Override
    public WritablePipelineProperty createProperty(JSONObject data) {
        return new WritablePipelineProperty(PipelineFieldType.Camera,
                new Supplier<Camera>() {
                    @Override
                    public Camera get() {
                        return null;
                    }
                });
    }
}
