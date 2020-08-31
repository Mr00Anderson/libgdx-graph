package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gempukku.libgdx.graph.data.FieldType;

public enum ShaderFieldType implements FieldType {
    Float("float"), Vector2("vec2"), Vector3("vec3"), Color("vec4"), Boolean("bool"),
    TextureRegion("vec4");

    private String shaderType;

    ShaderFieldType(String shaderType) {
        this.shaderType = shaderType;
    }

    @Override
    public boolean accepts(Object value) {
        switch (this) {
            case Float:
                return value instanceof Float;
            case Vector2:
                return value instanceof com.badlogic.gdx.math.Vector2;
            case Vector3:
                return value instanceof com.badlogic.gdx.math.Vector3;
            case Color:
                return value instanceof com.badlogic.gdx.graphics.Color;
            case Boolean:
                return value instanceof Boolean;
            case TextureRegion:
                return value instanceof TextureRegion;
        }
        return false;
    }

    public String getShaderType() {
        return shaderType;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }
}
