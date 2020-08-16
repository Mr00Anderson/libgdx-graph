package com.gempukku.libgdx.graph.libgdx;

import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.gempukku.libgdx.graph.renderer.PipelineRendererModels;

import java.util.HashSet;
import java.util.Set;

public class LibGDXModels implements PipelineRendererModels {
    private Set<RenderableProvider> models = new HashSet<>();

    public void addRenderableProvider(RenderableProvider renderableProvider) {
        models.add(renderableProvider);
    }

    public void removeRenderableProvider(RenderableProvider renderableProvider) {
        models.remove(renderableProvider);
    }

    @Override
    public Set<RenderableProvider> getModels() {
        return models;
    }
}
