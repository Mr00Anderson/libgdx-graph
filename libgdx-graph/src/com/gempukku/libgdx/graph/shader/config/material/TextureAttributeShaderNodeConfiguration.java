package com.gempukku.libgdx.graph.shader.config.material;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class TextureAttributeShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public TextureAttributeShaderNodeConfiguration(String type, String name) {
        super(type, name, "Material");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("texture", "Texture", ShaderFieldType.TextureRegion));
    }
}
