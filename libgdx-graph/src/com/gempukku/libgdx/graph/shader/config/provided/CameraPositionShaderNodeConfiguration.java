package com.gempukku.libgdx.graph.shader.config.provided;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class CameraPositionShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public CameraPositionShaderNodeConfiguration() {
        super("CameraPosition", "Camera position", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("position", "Position", ShaderFieldType.Vector3));
    }
}
