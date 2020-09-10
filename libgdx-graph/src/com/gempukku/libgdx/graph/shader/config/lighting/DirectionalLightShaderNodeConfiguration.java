package com.gempukku.libgdx.graph.shader.config.lighting;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class DirectionalLightShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public DirectionalLightShaderNodeConfiguration() {
        super("DirectionalLight", "Directional light", "Lighting");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("direction", "Direction", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("color", "Color", ShaderFieldType.Color));
    }
}
