package com.gempukku.libgdx.graph.renderer.impl;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.renderer.PipelineProperty;
import com.google.common.base.Supplier;

public class WritablePipelineProperty<T extends FieldType> implements PipelineProperty<T> {
    private boolean useDefault = true;
    private boolean initialized = false;
    private Object value;

    private T propertyType;
    private Supplier<?> defaultValueSupplier;

    public WritablePipelineProperty(T propertyType, Supplier<?> defaultValueSupplier) {
        this.propertyType = propertyType;
        this.defaultValueSupplier = defaultValueSupplier;
    }

    @Override
    public T getPropertyType() {
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

    @Override
    public Object getValue() {
        if (!initialized && useDefault) {
            value = defaultValueSupplier.get();
            initialized = true;
        }
        return value;
    }
}
