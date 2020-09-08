package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;

public enum PipelineFieldType implements FieldType {
    Float, Vector2, Vector3, Color, Boolean,
    Stage, Camera, Lights, Models, GraphModels, GraphLights,
    RenderPipeline;

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
            case RenderPipeline:
                return value instanceof com.gempukku.libgdx.graph.pipeline.RenderPipeline;
            case Stage:
                return value instanceof com.badlogic.gdx.scenes.scene2d.Stage;
            case Models:
                return value instanceof PipelineRendererModels;
            case GraphModels:
                return value instanceof GraphShaderModels;
            case Lights:
                return value instanceof Environment;
            case GraphLights:
                return value instanceof GraphShaderEnvironment;
            case Camera:
                return value instanceof com.badlogic.gdx.graphics.Camera;
        }
        return false;
    }

    @Override
    public Object convert(Object value) {
        return value;
    }
}
