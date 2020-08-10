package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;

import java.util.List;

public interface PipelineNodeInput {
    boolean isRequired();

    boolean isMainConnection();

    String getFieldName();

    String getFieldId();

    InputPropertyType getPropertyType();

    interface InputPropertyType {
        List<PropertyType> getAcceptedPropertyTypes();
    }
}
