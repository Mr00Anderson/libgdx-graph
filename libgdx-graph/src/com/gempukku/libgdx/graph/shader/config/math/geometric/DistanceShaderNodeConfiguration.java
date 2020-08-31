package com.gempukku.libgdx.graph.shader.config.math.geometric;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.ValidateSameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class DistanceShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public DistanceShaderNodeConfiguration() {
        super("Distance", "Distance", "Math/Geometric");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("p0", "Point 0", true, ShaderFieldType.Color, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("p1", "Point 1", true, ShaderFieldType.Color, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Result",
                        new ValidateSameTypeOutputTypeFunction<ShaderFieldType>(ShaderFieldType.Float, "p0", "p1"),
                        ShaderFieldType.Float));
    }
}
