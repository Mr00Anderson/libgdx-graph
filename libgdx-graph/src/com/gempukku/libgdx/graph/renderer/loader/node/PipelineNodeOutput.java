package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;

import java.util.List;

public interface PipelineNodeOutput {
    boolean isMainConnection();

    String getFieldName();

    String getFieldId();

    List<PropertyType> getProducablePropertyTypes();

    boolean supportsMultiple();
}
