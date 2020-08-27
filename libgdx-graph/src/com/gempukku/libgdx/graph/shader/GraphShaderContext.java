package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.TimeProvider;

public interface GraphShaderContext {
    PropertySource getPropertySource(String name);

    TimeProvider getTimeProvider();

    void addManagedResource(Disposable disposable);
}
