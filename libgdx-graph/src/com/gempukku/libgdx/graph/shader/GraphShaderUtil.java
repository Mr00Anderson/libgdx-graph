package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class GraphShaderUtil {
    private GraphShaderUtil() {
    }

    public static void addShaderTag(Model model, String tag) {
        addShaderTag(model.materials, tag);
    }

    public static void addShaderTag(ModelInstance modelInstance, String tag) {
        addShaderTag(modelInstance.materials, tag);
    }

    public static void removeShaderTag(Model model, String tag) {
        removeShaderTag(model.materials, tag);
    }

    public static void removeShaderTag(ModelInstance modelInstance, String tag) {
        removeShaderTag(modelInstance.materials, tag);
    }

    public static void setProperty(Model model, String property, Object value) {
        setProperty(model.materials, property, value);
    }

    public static void setProperty(ModelInstance modelInstance, String property, Object value) {
        setProperty(modelInstance.materials, property, value);
    }

    public static void removeProperty(Model model, String property) {
        removeProperty(model.materials, property);
    }

    public static void removeProperty(ModelInstance modelInstance, String property) {
        removeProperty(modelInstance.materials, property);
    }

    private static void addShaderTag(Iterable<Material> materials, String tag) {
        for (Material material : materials) {
            getOrCreate(material).addShaderTag(tag);
        }
    }

    private static void removeShaderTag(Iterable<Material> materials, String tag) {
        for (Material material : materials) {
            getOrCreate(material).removeShaderTag(tag);
        }
    }

    private static void setProperty(Iterable<Material> materials, String property, Object value) {
        for (Material material : materials) {
            getOrCreate(material).setProperty(property, value);
        }
    }

    private static void removeProperty(Iterable<Material> materials, String property) {
        for (Material material : materials) {
            getOrCreate(material).removeProperty(property);
        }
    }

    private static GraphShaderAttribute getOrCreate(Material material) {
        GraphShaderAttribute attribute = material.get(GraphShaderAttribute.class, GraphShaderAttribute.GraphShader);
        if (attribute == null) {
            attribute = new GraphShaderAttribute();
            material.set(attribute);
        }
        return attribute;
    }
}
