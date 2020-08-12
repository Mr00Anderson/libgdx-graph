package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;

import java.util.List;

public interface PipelineNodeOutput {
    boolean isMainConnection();

    String getFieldName();

    String getFieldId();

    OutputPropertyType getPropertyType();

    boolean supportsMultiple();

    interface OutputPropertyType {
        boolean mayProduce(PropertyType propertyType);

        PropertyType determinePropertyType(List<PropertyType> acceptedTypes);
    }
}
