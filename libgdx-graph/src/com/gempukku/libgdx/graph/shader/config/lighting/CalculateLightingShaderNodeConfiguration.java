package com.gempukku.libgdx.graph.shader.config.lighting;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class CalculateLightingShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public CalculateLightingShaderNodeConfiguration() {
        super("Lighting", "Calculate Lighting", "Lighting");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("position", "Position", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("normal", "Normal", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("albedo", "Albedo", false, ShaderFieldType.Color, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("emission", "Emission", false, ShaderFieldType.Color, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("specular", "Specular", false, ShaderFieldType.Color, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("shininess", "Shininess", false, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Color", ShaderFieldType.Vector3));
//        addNodeOutput(
//                new GraphNodeOutputImpl<ShaderFieldType>("specularStrength", "Specular", ShaderFieldType.Float));
//        addNodeOutput(
//                new GraphNodeOutputImpl<ShaderFieldType>("diffuseStrength", "Diffuse", ShaderFieldType.Float));
    }
}
