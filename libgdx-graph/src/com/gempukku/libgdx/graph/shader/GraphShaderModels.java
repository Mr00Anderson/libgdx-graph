package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

public interface GraphShaderModels {
    Iterable<ModelInstance> getModelsWithTag(String tag);
}
