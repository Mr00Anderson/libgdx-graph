package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gempukku.libgdx.graph.data.FieldType;

public enum ShaderFieldType implements FieldType {
    Float, Vector2, Vector3, Color, Boolean,
    TextureRegion;

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
                return value instanceof com.badlogic.gdx.graphics.Texture || value instanceof TextureRegion;
        }
        return false;
    }

    @Override
    public Object convert(Object value) {
        if (this == TextureRegion && value instanceof Texture) {
            return new TextureRegion((Texture) value);
        }
        return value;
    }
}
