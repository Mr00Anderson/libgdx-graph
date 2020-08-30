package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.data.FieldType;

public interface GraphBoxOutputConnector<T extends FieldType> {
    enum Side {
        Right, Bottom;
    }

    Side getSide();

    float getOffset();

    String getFieldId();
}
