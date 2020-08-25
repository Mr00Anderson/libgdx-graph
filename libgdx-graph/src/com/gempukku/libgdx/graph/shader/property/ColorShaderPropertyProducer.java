package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.graphics.Color;
import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import org.json.simple.JSONObject;

public class ColorShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Color;
    }

    @Override
    public PropertySource createProperty(String name, JSONObject data) {
        final Color color = Color.valueOf((String) data.get("color"));
        return new PropertySource(name, ShaderFieldType.Color, color);
    }
}
