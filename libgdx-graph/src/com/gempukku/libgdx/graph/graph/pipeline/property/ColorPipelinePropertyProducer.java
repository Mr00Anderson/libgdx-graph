package com.gempukku.libgdx.graph.graph.pipeline.property;

import com.badlogic.gdx.graphics.Color;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class ColorPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Color");
    }

    @Override
    public WritablePipelineProperty createProperty(JSONObject data) {
        final Color color = Color.valueOf((String) data.get("color"));
        return new WritablePipelineProperty(PipelineFieldType.Color,
                new Supplier<Color>() {
                    @Override
                    public Color get() {
                        return color;
                    }
                });
    }
}
