package com.gempukku.libgdx.graph.renderer.impl;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.renderer.PipelineProperty;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.google.common.base.Supplier;

public class WritablePipelineProperty implements PipelineProperty {
    private boolean useDefault = true;
    private boolean initialized = false;
    private Object value;

    private PropertyType propertyType;
    private Supplier<?> defaultValueSupplier;

    public WritablePipelineProperty(PropertyType propertyType, Supplier<?> defaultValueSupplier) {
        this.propertyType = propertyType;
        this.defaultValueSupplier = defaultValueSupplier;
    }

    @Override
    public PropertyType getPropertyType() {
        return propertyType;
    }

    public void setValue(Object value) {
        if (!useDefault) {
            if (value instanceof Disposable) {
                ((Disposable) value).dispose();
            }
        }
        this.value = value;
        useDefault = false;
    }

    public void unsetValue() {
        if (!useDefault) {
            useDefault = true;
            initialized = false;
        }
    }

    public Object getValue() {
        if (!initialized && useDefault) {
            value = defaultValueSupplier.get();
            initialized = true;
        }
        return value;
    }
}
