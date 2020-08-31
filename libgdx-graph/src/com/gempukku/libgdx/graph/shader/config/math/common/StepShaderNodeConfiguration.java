package com.gempukku.libgdx.graph.shader.config.math.common;

import com.gempukku.libgdx.graph.MathCommonOutputTypeFunction;
import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class StepShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public StepShaderNodeConfiguration() {
        super("Step", "Step", "Math/Common");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("edge", "Edge", true, ShaderFieldType.Color, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("input", "Input", true, ShaderFieldType.Color, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Result",
                        new MathCommonOutputTypeFunction<ShaderFieldType>(ShaderFieldType.Float, new String[]{"input"}, new String[]{"edge"}),
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Color));
    }
}
