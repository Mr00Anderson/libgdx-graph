package com.gempukku.libgdx.graph.shader.config.math.common;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.SameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class LerpShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public LerpShaderNodeConfiguration() {
        super("Mix", "Mix (lerp)", "Math/Common");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("a", "A", true, ShaderFieldType.Color, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("b", "B", true, ShaderFieldType.Color, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("t", "T", true, ShaderFieldType.Color, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Result",
                        new SameTypeOutputTypeFunction<ShaderFieldType>("a"),
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Color));
    }
}
