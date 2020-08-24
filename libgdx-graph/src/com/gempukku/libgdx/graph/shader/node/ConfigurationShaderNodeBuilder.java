package com.gempukku.libgdx.graph.shader.node;

import com.gempukku.libgdx.graph.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import org.json.simple.JSONObject;

public abstract class ConfigurationShaderNodeBuilder implements GraphShaderNodeBuilder {
    private NodeConfiguration<ShaderFieldType> configuration;

    public ConfigurationShaderNodeBuilder(NodeConfiguration<ShaderFieldType> configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public NodeConfiguration<ShaderFieldType> getConfiguration(JSONObject data) {
        return configuration;
    }
}
