package com.gempukku.libgdx.graph.shader.property;

import com.gempukku.libgdx.WhitePixel;
import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import org.json.simple.JSONObject;

public class TextureShaderPropertyProducer implements GraphShaderPropertyProducer {
    @Override
    public ShaderFieldType getType() {
        return ShaderFieldType.TextureRegion;
    }

    @Override
    public PropertySource createProperty(String name, JSONObject data, boolean designTime) {
        if (designTime)
            return new PropertySource(name, ShaderFieldType.TextureRegion, WhitePixel.sharedInstance.textureRegion);
        else
            return new PropertySource(name, ShaderFieldType.TextureRegion, null);
    }
}
