package com.gempukku.libgdx.graph.data;

import java.util.List;

public interface GraphNodeInput<T extends FieldType> {
    boolean isRequired();

    boolean isMainConnection();

    String getFieldName();

    String getFieldId();

    List<? extends T> getAcceptedPropertyTypes();
}
