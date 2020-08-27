package com.gempukku.libgdx.graph.shader.config.noise;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class SimplexNoise3DNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public SimplexNoise3DNodeConfiguration() {
        super("SimplexNoise3D", "Simplex Noise 3D", "Noise");
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("point", "Point", true, ShaderFieldType.Vector3));
        addNodeInput(
                new GraphNodeInputImpl<ShaderFieldType>("scale", "Scale", true, ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("output", "Result", ShaderFieldType.Float));
    }
}
