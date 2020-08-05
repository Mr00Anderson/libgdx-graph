package com.gempukku.libgdx.graph.renderer.loader;

import com.gempukku.libgdx.graph.renderer.PropertyType;

public class PipelineNodeOutputImpl implements PipelineNodeOutput {
    private String name;
    private PropertyType type;

    public PipelineNodeOutputImpl(String name, PropertyType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public PropertyType getType() {
        return type;
    }
}
