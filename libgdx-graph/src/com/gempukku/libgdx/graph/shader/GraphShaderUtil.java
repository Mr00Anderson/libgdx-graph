package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class GraphShaderUtil {
    private GraphShaderUtil() {
    }

    public static void setProperty(ModelInstance modelInstance, String property, Object value) {
        for (Material material : modelInstance.materials) {
            GraphShaderAttribute graphShaderAttribute = material.get(GraphShaderAttribute.class, GraphShaderAttribute.GraphShader);
            graphShaderAttribute.setProperty(property, value);
        }
    }
}
