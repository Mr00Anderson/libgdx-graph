package com.gempukku.libgdx.graph.shader.config.color;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class IntensityShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public IntensityShaderNodeConfiguration() {
        super("Intensity", "Intensity (Luma)", "Color");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("color", "Color", true, ShaderFieldType.Color, ShaderFieldType.Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Intensity", ShaderFieldType.Float));
    }
}
