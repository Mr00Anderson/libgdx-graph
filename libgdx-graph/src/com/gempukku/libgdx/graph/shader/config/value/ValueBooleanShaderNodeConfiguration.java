package com.gempukku.libgdx.graph.shader.config.value;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class ValueBooleanShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ValueBooleanShaderNodeConfiguration() {
        super("ValueBoolean", "Boolean", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("value", "Value", ShaderFieldType.Boolean));
    }
}
