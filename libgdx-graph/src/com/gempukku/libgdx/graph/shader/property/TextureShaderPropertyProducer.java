package com.gempukku.libgdx.graph.shader.property;

import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import org.json.simple.JSONObject;

public class TextureShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("TextureRegion");
    }

    @Override
    public PropertySource createProperty(String name, JSONObject data) {
        return new PropertySource(name, ShaderFieldType.TextureRegion, null);
    }
}
