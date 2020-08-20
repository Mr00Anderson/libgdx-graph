package com.gempukku.libgdx.graph.ui.producer;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;

import java.util.Arrays;
import java.util.List;

public class ValueGraphNodeOutput<T extends FieldType> implements GraphNodeOutput<T> {
    private String fieldName;
    private List<? extends T> propertyTypes;

    public ValueGraphNodeOutput(String fieldName, T propertyType) {
        this.fieldName = fieldName;
        this.propertyTypes = Arrays.asList(propertyType);
    }

    @Override
    public boolean isMainConnection() {
        return false;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getFieldId() {
        return "value";
    }

    @Override
    public List<? extends T> getProducablePropertyTypes() {
        return propertyTypes;
    }

    @Override
    public boolean supportsMultiple() {
        return true;
    }
}
