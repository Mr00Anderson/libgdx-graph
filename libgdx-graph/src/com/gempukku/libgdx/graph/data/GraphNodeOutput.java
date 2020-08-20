package com.gempukku.libgdx.graph.data;

import java.util.List;

public interface GraphNodeOutput<T extends FieldType> {
    boolean isMainConnection();

    String getFieldName();

    String getFieldId();

    List<? extends T> getProducablePropertyTypes();

    boolean supportsMultiple();
}
