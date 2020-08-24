package com.gempukku.libgdx.graph.shader.property;

import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import org.json.simple.JSONObject;

public class FloatShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Float");
    }

    @Override
    public PropertySource createProperty(String name, JSONObject data) {
        final float x = ((Number) data.get("x")).floatValue();
        return new PropertySource(name, ShaderFieldType.Float, x);
    }
}
