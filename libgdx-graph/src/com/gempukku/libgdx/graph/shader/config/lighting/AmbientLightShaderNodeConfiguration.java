package com.gempukku.libgdx.graph.shader.config.lighting;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class AmbientLightShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public AmbientLightShaderNodeConfiguration() {
        super("AmbientLight", "Ambient light", "Lighting");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("ambient", "Color", ShaderFieldType.Vector3));
    }
}
