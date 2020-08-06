package com.gempukku.libgdx.graph.renderer;

import com.badlogic.gdx.graphics.g3d.Environment;

public enum PropertyType {
    Vector1(true), Vector2(true), Vector3(true),
    Color(true), TextureRegion(true),
    Boolean(true), RenderPipeline(false),
    Stage(true), Camera(true),
    Lights(true), Models(true);

    private boolean supportsMultiple;

    PropertyType(boolean supportsMultiple) {
        this.supportsMultiple = supportsMultiple;
    }

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
            case RenderPipeline:
                return value instanceof RenderPipeline;
            case Stage:
                return value instanceof com.badlogic.gdx.scenes.scene2d.Stage;
            case Models:
                return value instanceof PipelineRendererModels;
            case Lights:
                return value instanceof Environment;
            case Camera:
                return value instanceof com.badlogic.gdx.graphics.Camera;

        }
        return false;
    }

    public boolean supportsMultiple() {
        return supportsMultiple;
    }
}
