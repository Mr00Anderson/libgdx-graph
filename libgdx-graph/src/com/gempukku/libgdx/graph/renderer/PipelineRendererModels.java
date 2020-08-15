package com.gempukku.libgdx.graph.renderer;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

public interface PipelineRendererModels {
    Iterable<? extends ModelInstance> getModels();
}
