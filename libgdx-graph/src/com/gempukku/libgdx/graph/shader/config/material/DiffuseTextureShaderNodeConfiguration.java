package com.gempukku.libgdx.graph.shader.config.material;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class DiffuseTextureShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public DiffuseTextureShaderNodeConfiguration() {
        super("DiffuseTexture", "Diffuse texture");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("texture", "Texture", ShaderFieldType.TextureRegion));
    }
}
