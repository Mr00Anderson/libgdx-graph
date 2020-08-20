package com.gempukku.libgdx.graph.renderer;

import com.gempukku.libgdx.graph.data.FieldType;

public interface PipelineProperty<T extends FieldType> {
    T getPropertyType();

    Object getValue();
}
