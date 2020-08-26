package com.gempukku.libgdx.graph.shader.config.attribute;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class AttributeNormalShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public AttributeNormalShaderNodeConfiguration() {
        super("AttributeNormal", "Normal attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("normal", "Normal", ShaderFieldType.Vector3));
    }
}
