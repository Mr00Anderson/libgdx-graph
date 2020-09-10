package com.gempukku.libgdx.graph.shader.config.lighting;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class PointLightShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public PointLightShaderNodeConfiguration() {
        super("PointLight", "Point light", "Lighting");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("position", "Position", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("color", "Color", ShaderFieldType.Color));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("intensity", "Intensity", ShaderFieldType.Float));
    }
}
