package com.gempukku.libgdx.graph.renderer.property;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class StagePipelinePropertyProducer implements PipelinePropertyProducer<PipelineFieldType> {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Stage");
    }

    @Override
    public WritablePipelineProperty<PipelineFieldType> createProperty(JSONObject data) {
        return new WritablePipelineProperty<PipelineFieldType>(PipelineFieldType.Stage,
                new Supplier<Stage>() {
                    @Override
                    public Stage get() {
                        return null;
                    }
                });
    }
}
