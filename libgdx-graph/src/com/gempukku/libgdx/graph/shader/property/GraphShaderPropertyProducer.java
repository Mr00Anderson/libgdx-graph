package com.gempukku.libgdx.graph.shader.property;

import com.gempukku.libgdx.graph.shader.PropertySource;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import org.json.simple.JSONObject;

public interface GraphShaderPropertyProducer {
    ShaderFieldType getType();

    PropertySource createProperty(String name, JSONObject data, boolean designTime);
}
