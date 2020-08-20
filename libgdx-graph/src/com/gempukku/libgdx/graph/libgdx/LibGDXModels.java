package com.gempukku.libgdx.graph.libgdx;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererModels;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class LibGDXModels implements PipelineRendererModels {
    private Set<RenderableProvider> models = new HashSet<>();

    private Map<Material, Set<String>> tags = new WeakHashMap<>();

    public void addRenderableProvider(RenderableProvider renderableProvider) {
        models.add(renderableProvider);
    }

    public void removeRenderableProvider(RenderableProvider renderableProvider) {
        models.remove(renderableProvider);
    }

    public void addShaderTag(Material material, String tag) {
        Set<String> tagSet = tags.get(material);
        if (tagSet == null) {
            tagSet = new HashSet<>();
            tags.put(material, tagSet);
        }
        tagSet.add(tag);
    }

    public void removeShaderTag(Material material, String tag) {
        Set<String> tagSet = tags.get(material);
        if (tagSet != null) {
            tagSet.remove(tag);
        }
    }

    @Override
    public Set<RenderableProvider> getModels() {
        return models;
    }
}
