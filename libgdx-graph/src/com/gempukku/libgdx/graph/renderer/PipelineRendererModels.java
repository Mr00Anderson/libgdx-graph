package com.gempukku.libgdx.graph.renderer;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.HashSet;
import java.util.Set;

public class PipelineRendererModels {
    private Set<ModelInstance> models = new HashSet<>();

    public void addModelInstance(ModelInstance modelInstance) {
        models.add(modelInstance);
    }

    public void removeModelInstance(ModelInstance modelInstance) {
        models.remove(modelInstance);
    }

    public Set<ModelInstance> getModels() {
        return models;
    }
}
