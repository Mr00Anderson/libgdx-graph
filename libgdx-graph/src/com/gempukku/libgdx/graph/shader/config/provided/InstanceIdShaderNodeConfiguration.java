package com.gempukku.libgdx.graph.shader.config.provided;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class InstanceIdShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public InstanceIdShaderNodeConfiguration() {
        super("InstanceID", "Instance ID", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("id", "Id", ShaderFieldType.Float));
    }
}
