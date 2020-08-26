package com.gempukku.libgdx.graph.shader.config.attribute;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class AttributeUVShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public AttributeUVShaderNodeConfiguration() {
        super("AttributeUV", "UV attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("uv", "UV", ShaderFieldType.Vector2));
    }
}
