package com.gempukku.libgdx.graph.shader.config.provided;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class CameraDirectionShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public CameraDirectionShaderNodeConfiguration() {
        super("CameraDirection", "Camera direction", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("direction", "Direction", ShaderFieldType.Vector3));
    }
}
