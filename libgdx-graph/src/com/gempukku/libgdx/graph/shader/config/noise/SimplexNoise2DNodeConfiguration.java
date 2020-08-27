package com.gempukku.libgdx.graph.shader.config.noise;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class SimplexNoise2DNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SimplexNoise2DNodeConfiguration() {
        super("SimplexNoise2D", "Simplex Noise 2D", "Noise");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("uv", "UV", true, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("scale", "Scale", true, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Result", ShaderFieldType.Float));
    }
}
