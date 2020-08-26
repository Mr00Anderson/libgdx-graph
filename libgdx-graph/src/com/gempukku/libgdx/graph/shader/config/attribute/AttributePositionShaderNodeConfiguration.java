package com.gempukku.libgdx.graph.shader.config.attribute;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class AttributePositionShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public AttributePositionShaderNodeConfiguration() {
        super("AttributePosition", "Position attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("position", "Position", ShaderFieldType.Vector3));
    }
}
