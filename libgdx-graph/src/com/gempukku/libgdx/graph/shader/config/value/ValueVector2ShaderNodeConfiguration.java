package com.gempukku.libgdx.graph.shader.config.value;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

import static com.gempukku.libgdx.graph.shader.ShaderFieldType.Vector2;

public class ValueVector2ShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ValueVector2ShaderNodeConfiguration() {
        super("ValueVector2", "Vector2", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("value", "Value", Vector2));
    }
}
