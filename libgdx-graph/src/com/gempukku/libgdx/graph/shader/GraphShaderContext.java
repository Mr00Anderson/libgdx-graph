package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.utils.Disposable;

public interface GraphShaderContext {
    PropertySource getPropertySource(String name);

    void addManagedResource(Disposable disposable);
}
