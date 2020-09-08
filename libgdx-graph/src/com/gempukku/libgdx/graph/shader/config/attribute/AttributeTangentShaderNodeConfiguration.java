package com.gempukku.libgdx.graph.shader.config.attribute;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class AttributeTangentShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public AttributeTangentShaderNodeConfiguration() {
        super("AttributeTangent", "Tangent attribute", "Attribute");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("tangent", "Tangent", ShaderFieldType.Vector3));
    }
}
