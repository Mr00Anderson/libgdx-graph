package com.gempukku.libgdx.graph.data;

import org.json.simple.JSONObject;

public interface GraphProperty<T extends FieldType> {
    String getName();

    T getType();

    JSONObject getData();
}
