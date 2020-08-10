package com.gempukku.libgdx.graph.ui.producer;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutput;

public class ValuePipelineNodeOutput implements PipelineNodeOutput {
    private String fieldName;
    private PropertyType propertyType;

    public ValuePipelineNodeOutput(String fieldName, PropertyType propertyType) {
        this.fieldName = fieldName;
        this.propertyType = propertyType;
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
    public OutputPropertyType getPropertyType() {
        return new OutputPropertyType() {
            @Override
            public boolean mayProduce(PropertyType propertyType) {
                return propertyType == ValuePipelineNodeOutput.this.propertyType;
            }

            @Override
            public PropertyType determinePropertyType() {
                return propertyType;
            }
        };
    }

    @Override
    public boolean supportsMultiple() {
        return true;
    }
}
