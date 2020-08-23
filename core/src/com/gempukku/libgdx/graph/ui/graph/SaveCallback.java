package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.data.FieldType;

public interface SaveCallback<T extends FieldType> {
    void save(GraphDesignTab<T> graphDesignTab);
}
