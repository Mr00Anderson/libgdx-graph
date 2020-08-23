package com.gempukku.libgdx.graph.shader;

import com.gempukku.libgdx.graph.data.FieldType;

public enum ShaderFieldType implements FieldType {
    Float, Vector2, Vector3, Color, Boolean;

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
        }
        return false;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }
}
