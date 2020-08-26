package com.gempukku.libgdx.graph.shader.config.value;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;

import static com.gempukku.libgdx.graph.shader.ShaderFieldType.Vector3;

public class ValueVector3ShaderNodeConfiguration extends NodeConfigurationImpl<ShaderFieldType> {
    public ValueVector3ShaderNodeConfiguration() {
        super("ValueVector3", "Vector3", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl<ShaderFieldType>("value", "Value", Vector3));
    }
}
