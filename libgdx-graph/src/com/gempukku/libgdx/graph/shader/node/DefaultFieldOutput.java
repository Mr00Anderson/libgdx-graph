package com.gempukku.libgdx.graph.shader.node;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class DefaultFieldOutput implements GraphShaderNodeBuilder.FieldOutput {
    private ShaderFieldType fieldType;
    private String representation;

    public DefaultFieldOutput(ShaderFieldType fieldType, String representation) {
        this.fieldType = fieldType;
        this.representation = representation;
    }

    @Override
    public ShaderFieldType getFieldType() {
        return fieldType;
    }

    @Override
    public String getRepresentation() {
        return representation;
    }
}
