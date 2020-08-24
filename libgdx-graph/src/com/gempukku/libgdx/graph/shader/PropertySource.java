package com.gempukku.libgdx.graph.shader;

public class PropertySource {
    private String propertyName;
    private ShaderFieldType shaderFieldType;
    private Object defaultValue;

    public PropertySource(String propertyName, ShaderFieldType shaderFieldType, Object defaultValue) {
        this.propertyName = propertyName;
        this.shaderFieldType = shaderFieldType;
        this.defaultValue = defaultValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public ShaderFieldType getShaderFieldType() {
        return shaderFieldType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }
}
