package com.gempukku.libgdx.graph.shader.config.provided;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class TimeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public TimeShaderNodeConfiguration() {
        super("Time", "Time", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("time", "Time", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("sinTime", "sin(Time)", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("cosTime", "cos(Time)", ShaderFieldType.Float));
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("deltaTime", "deltaTime", ShaderFieldType.Float));
    }
}
