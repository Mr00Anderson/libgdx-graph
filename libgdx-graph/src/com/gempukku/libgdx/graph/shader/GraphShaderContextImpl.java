package com.gempukku.libgdx.graph.shader;

import java.util.Map;

public class GraphShaderContextImpl implements GraphShaderContext {
    private Map<String, PropertySource> propertySourceMap;

    public GraphShaderContextImpl(Map<String, PropertySource> propertySourceMap) {
        this.propertySourceMap = propertySourceMap;
    }

    @Override
    public PropertySource getPropertySource(String name) {
        return propertySourceMap.get(name);
    }
}
