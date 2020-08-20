package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.graphics.g3d.RenderableProvider;

public interface PipelineRendererModels {
    Iterable<? extends RenderableProvider> getModels();
}
