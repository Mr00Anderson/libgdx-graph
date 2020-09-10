package com.gempukku.libgdx.graph.shader.config.lighting;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class SpotLightShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SpotLightShaderNodeConfiguration() {
        super("SpotLight", "Spot light", "Lighting");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("position", "Position", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("direction", "Direction", ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("color", "Color", ShaderFieldType.Color));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("intensity", "Intensity", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("cutOffAngle", "Cut off angle", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("exponent", "Exponent", ShaderFieldType.Float));
    }
}
