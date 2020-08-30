package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.data.FieldType;

public interface GraphBoxInputConnector<T extends FieldType> {
    enum Side {
        Left, Top;
    }

    Side getSide();

    float getOffset();

    String getFieldId();
}
