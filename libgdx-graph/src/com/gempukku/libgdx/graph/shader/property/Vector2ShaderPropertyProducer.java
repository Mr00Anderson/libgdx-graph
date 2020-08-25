package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import org.json.simple.JSONObject;

public class Vector2ShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.Vector2;
    }

    @Override
    public PropertySource createProperty(String name, JSONObject data, Object propertyDefaultFallback) {
        final float x = ((Number) data.get("x")).floatValue();
        final float y = ((Number) data.get("y")).floatValue();
        return new PropertySource(name, ShaderFieldType.Vector2, new Vector2(x, y));
    }
}
