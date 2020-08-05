package com.gempukku.libgdx.graph.renderer.loader;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.google.common.base.Predicate;

public class PipelineNodeInputImpl implements PipelineNodeInput {
    private boolean required;
    private String name;
    private Predicate<PropertyType> acceptedTypes;

    public PipelineNodeInputImpl(boolean required, String name, Predicate<PropertyType> acceptedTypes) {
        this.required = required;
        this.name = name;
        this.acceptedTypes = acceptedTypes;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Predicate<PropertyType> getAcceptedTypes() {
        return acceptedTypes;
    }
}
