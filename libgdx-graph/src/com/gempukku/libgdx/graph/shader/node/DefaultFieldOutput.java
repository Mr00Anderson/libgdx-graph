package com.gempukku.libgdx.graph.shader.node;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;

public class DefaultFieldOutput implements GraphShaderNodeBuilder.FieldOutput {
    private ShaderFieldType fieldType;
    private String representation;
    private String samplerRepresentation;

    public DefaultFieldOutput(ShaderFieldType fieldType, String representation) {
        this(fieldType, representation, null);
    }

    public DefaultFieldOutput(ShaderFieldType fieldType, String representation, String samplerRepresentation) {
        this.fieldType = fieldType;
        this.representation = representation;
        this.samplerRepresentation = samplerRepresentation;
    }

    @Override
    public ShaderFieldType getFieldType() {
        return fieldType;
    }

    @Override
    public String getRepresentation() {
        return representation;
    }

    @Override
    public String getSamplerRepresentation() {
        return samplerRepresentation;
    }
}
