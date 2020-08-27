package com.gempukku.libgdx.graph.shader.config.math.arithmetic;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class PowerShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public PowerShaderNodeConfiguration() {
        super("Power", "Power", "Math/Arithmetic");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("a", "A", true, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("b", "B", true, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Result", ShaderFieldType.Float));
    }
}
