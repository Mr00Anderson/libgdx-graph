package com.gempukku.libgdx.graph.renderer.property;

import com.badlogic.gdx.graphics.Color;
import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class ColorPipelinePropertyProducer implements PipelinePropertyProducer<PipelineFieldType> {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Color");
    }

    @Override
    public WritablePipelineProperty<PipelineFieldType> createProperty(JSONObject data) {
        final Color color = Color.valueOf((String) data.get("color"));
        return new WritablePipelineProperty<PipelineFieldType>(PipelineFieldType.Color,
                new Supplier<Color>() {
                    @Override
                    public Color get() {
                        return color;
                    }
                });
    }
}
