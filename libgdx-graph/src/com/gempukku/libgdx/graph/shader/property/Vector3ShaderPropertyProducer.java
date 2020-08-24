package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import org.json.simple.JSONObject;

public class Vector3ShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Vector3");
    }

    @Override
    public PropertySource createProperty(String name, JSONObject data) {
        final float x = ((Number) data.get("x")).floatValue();
        final float y = ((Number) data.get("y")).floatValue();
        final float z = ((Number) data.get("z")).floatValue();
        return new PropertySource(name, ShaderFieldType.Vector3, new Vector3(x, y, z));
    }
}
