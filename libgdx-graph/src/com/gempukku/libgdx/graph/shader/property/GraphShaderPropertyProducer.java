package com.gempukku.libgdx.graph.shader.property;

import com.gempukku.libgdx.graph.shader.PropertySource;
import org.json.simple.JSONObject;

public interface GraphShaderPropertyProducer {
    boolean supportsType(String type);

    PropertySource createProperty(String name, JSONObject data);
}
