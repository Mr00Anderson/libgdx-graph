package com.gempukku.libgdx.graph.ui.producer;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutput;

import java.util.Arrays;
import java.util.List;

public class ValuePipelineNodeOutput implements PipelineNodeOutput {
    private String fieldName;
    private List<PropertyType> propertyTypes;

    public ValuePipelineNodeOutput(String fieldName, PropertyType propertyType) {
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
    public List<PropertyType> getProducablePropertyTypes() {
        return propertyTypes;
    }

    @Override
    public boolean supportsMultiple() {
        return true;
    }
}
