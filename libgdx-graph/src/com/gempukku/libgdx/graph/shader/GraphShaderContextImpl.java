package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.utils.Disposable;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphShaderContextImpl implements GraphShaderContext, Disposable {
    private List<Disposable> disposables = new LinkedList<>();
    private Map<String, PropertySource> propertySourceMap;

    public GraphShaderContextImpl(Map<String, PropertySource> propertySourceMap) {
        this.propertySourceMap = propertySourceMap;
    }

    @Override
    public PropertySource getPropertySource(String name) {
        return propertySourceMap.get(name);
    }

    @Override
    public void addManagedResource(Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    public void dispose() {
        for (Disposable disposable : disposables) {
            disposable.dispose();
        }
        disposables.clear();
    }
}
