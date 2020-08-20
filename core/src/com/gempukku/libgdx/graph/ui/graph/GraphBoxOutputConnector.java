package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;

public interface GraphBoxOutputConnector<T extends FieldType> extends GraphNodeOutput<T> {
    enum Side {
        Right, Bottom;
    }

    Side getSide();

    float getOffset();
}
