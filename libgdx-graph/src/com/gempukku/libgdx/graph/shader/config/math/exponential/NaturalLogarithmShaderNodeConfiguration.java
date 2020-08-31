package com.gempukku.libgdx.graph.shader.config.math.exponential;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.SameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class NaturalLogarithmShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public NaturalLogarithmShaderNodeConfiguration() {
        super("Log", "Log e", "Math/Exponential");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("input", "Input", true, ShaderFieldType.Color, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Result",
                        new SameTypeOutputTypeFunction<ShaderFieldType>("input"),
                        ShaderFieldType.Color, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
    }
}
