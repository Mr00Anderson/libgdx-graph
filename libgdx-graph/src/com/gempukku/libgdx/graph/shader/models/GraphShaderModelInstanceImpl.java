package com.gempukku.libgdx.graph.shader.models;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphShaderModelInstanceImpl implements GraphShaderModelInstance {
    private String id;
    private GraphShaderModel model;
    private ModelInstance modelInstance;
    private Set<String> tags = new HashSet<>();
    private Map<String, Object> properties = new HashMap<>();

    public GraphShaderModelInstanceImpl(String id, GraphShaderModel model, ModelInstance modelInstance) {
        this.id = id;
        this.model = model;
        this.modelInstance = modelInstance;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void addTag(String tag) {
        tags.add(tag);
    }

    @Override
    public void removeTag(String tag) {
        tags.remove(tag);
    }

    @Override
    public void setProperty(String name, Object value) {
        properties.put(name, value);
    }

    @Override
    public void unsetProperty(String name) {
        properties.remove(name);
    }

    @Override
    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    @Override
    public Object getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public Matrix4 getTransformMatrix() {
        return modelInstance.transform;
    }

    public GraphShaderModel getModel() {
        return model;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        modelInstance.getRenderables(renderables, pool);
    }
}
