package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.graph.pipeline.PropertyType;

public interface GraphBoxOutputConnector {
    enum Side {
        Right, Bottom;
    }

    String getId();

    Side getSide();

    PropertyType getPropertyType();

    float getOffset();
}
