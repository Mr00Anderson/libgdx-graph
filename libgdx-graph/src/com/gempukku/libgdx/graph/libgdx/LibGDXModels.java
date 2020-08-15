package com.gempukku.libgdx.graph.libgdx;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.gempukku.libgdx.graph.renderer.PipelineRendererModels;
import com.gempukku.libgdx.graph.shader.GraphShaderModels;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class LibGDXModels implements PipelineRendererModels, GraphShaderModels {
    private Set<ModelInstance> models = new HashSet<>();
    private Multimap<ModelInstance, String> tags = HashMultimap.create();
    private Multimap<String, ModelInstance> reverseTags = HashMultimap.create();

    public void addModelInstance(ModelInstance modelInstance) {
        models.add(modelInstance);
    }

    public void removeModelInstance(ModelInstance modelInstance) {
        models.remove(modelInstance);
        Collection<String> tags = this.tags.removeAll(modelInstance);
        for (String tag : tags) {
            reverseTags.remove(tag, modelInstance);
        }
    }

    public void addModelTag(ModelInstance modelInstance, String tag) {
        if (!models.contains(modelInstance))
            throw new IllegalArgumentException("Model instance not found");
        tags.put(modelInstance, tag);
        reverseTags.put(tag, modelInstance);
    }

    public void removeModelTag(ModelInstance modelInstance, String tag) {
        if (!models.contains(modelInstance))
            throw new IllegalArgumentException("Model instance not found");
        tags.remove(modelInstance, tag);
        reverseTags.remove(tag, modelInstance);
    }

    @Override
    public Set<ModelInstance> getModels() {
        return models;
    }

    @Override
    public Iterable<ModelInstance> getModelsWithTag(String tag) {
        return reverseTags.get(tag);
    }
}
