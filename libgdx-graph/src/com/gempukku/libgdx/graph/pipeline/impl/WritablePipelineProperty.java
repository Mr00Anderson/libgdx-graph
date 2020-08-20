package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.PipelineProperty;
import com.google.common.base.Supplier;

public class WritablePipelineProperty implements PipelineProperty {
    private boolean useDefault = true;
    private boolean initialized = false;
    private Object value;

    private PipelineFieldType propertyType;
    private Supplier<?> defaultValueSupplier;

    public WritablePipelineProperty(PipelineFieldType propertyType, Supplier<?> defaultValueSupplier) {
        this.propertyType = propertyType;
        this.defaultValueSupplier = defaultValueSupplier;
    }

    @Override
    public PipelineFieldType getType() {
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
