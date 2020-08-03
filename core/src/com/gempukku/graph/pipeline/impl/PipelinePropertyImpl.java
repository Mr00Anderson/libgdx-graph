package com.gempukku.graph.pipeline.impl;

import com.gempukku.graph.pipeline.PipelineProperty;
import com.gempukku.graph.pipeline.PropertyType;

public class PipelinePropertyImpl implements PipelineProperty {
    private PropertyType propertyType;
    private Object value;

    public PipelinePropertyImpl(PropertyType propertyType) {
        this.propertyType = propertyType;
    }

    @Override
    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
