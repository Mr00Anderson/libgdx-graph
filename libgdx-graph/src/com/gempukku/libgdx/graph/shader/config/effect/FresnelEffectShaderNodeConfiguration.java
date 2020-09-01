package com.gempukku.libgdx.graph.shader.config.effect;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class FresnelEffectShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public FresnelEffectShaderNodeConfiguration() {
        super("FresnelEffect", "Fresnel effect", "Effect");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("normal", "Normal", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("viewDir", "View direction", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("power", "Power", true, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Output", ShaderFieldType.Float));
    }
}
