package com.gempukku.libgdx.graph.data;

public interface GraphProperty<T extends FieldType> {
    String getName();

    T getType();
}
