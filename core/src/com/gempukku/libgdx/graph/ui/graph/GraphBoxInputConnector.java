package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInput;

public interface GraphBoxInputConnector<T extends FieldType> extends GraphNodeInput<T> {
    enum Side {
        Left, Top;
    }

    Side getSide();

    float getOffset();
}
