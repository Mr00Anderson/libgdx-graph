package com.gempukku.libgdx.graph.shader.property;

import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import org.json.simple.JSONObject;

public class FloatShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Float;
    }

    @Override
    public PropertySource createProperty(String name, JSONObject data, boolean designTime) {
        final float x = ((Number) data.get("x")).floatValue();
        return new PropertySource(name, ShaderFieldType.Float, x);
    }
}
