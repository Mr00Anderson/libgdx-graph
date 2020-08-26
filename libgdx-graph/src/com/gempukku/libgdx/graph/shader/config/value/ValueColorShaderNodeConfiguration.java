package com.gempukku.libgdx.graph.shader.config.value;


import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

import static com.gempukku.libgdx.graph.shader.ShaderFieldType.Color;

public class ValueColorShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ValueColorShaderNodeConfiguration() {
        super("ValueColor", "Color", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("value", "Value", Color));
    }
}
