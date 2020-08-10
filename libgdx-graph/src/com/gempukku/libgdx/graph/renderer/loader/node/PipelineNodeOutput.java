package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;

public interface PipelineNodeOutput {
    boolean isMainConnection();

    String getFieldName();

    String getFieldId();

    OutputPropertyType getPropertyType();

    interface OutputPropertyType {
        boolean mayProduce(PropertyType propertyType);

        PropertyType determinePropertyType();
    }
}
