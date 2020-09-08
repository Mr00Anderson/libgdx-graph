package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.TimeProvider;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;

public interface GraphShaderContext {
    PropertySource getPropertySource(String name);

    TimeProvider getTimeProvider();

    GraphShaderEnvironment getEnvironment();

    void addManagedResource(Disposable disposable);
}
