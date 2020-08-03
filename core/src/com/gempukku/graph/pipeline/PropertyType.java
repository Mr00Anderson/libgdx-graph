package com.gempukku.graph.pipeline;

public enum PropertyType {
    Vector1, Vector2, Vector3, Color, TextureRegion, Boolean;

    public boolean accepts(Object value) {
        switch (this) {
            case Vector1:
                return value instanceof Float;
            case Vector2:
                return value instanceof com.badlogic.gdx.math.Vector2;
            case Vector3:
                return value instanceof com.badlogic.gdx.math.Vector3;
            case Color:
                return value instanceof com.badlogic.gdx.graphics.Color;
            case TextureRegion:
                return value instanceof com.badlogic.gdx.graphics.g2d.TextureRegion;
            case Boolean:
                return value instanceof Boolean;
        }
        return false;
    }
}
