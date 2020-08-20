package com.gempukku.libgdx.graph.pipeline.property;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class StagePipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Stage");
    }

    @Override
    public WritablePipelineProperty createProperty(JSONObject data) {
        return new WritablePipelineProperty(PipelineFieldType.Stage,
                new Supplier<Stage>() {
                    @Override
                    public Stage get() {
                        return null;
                    }
                });
    }
}
