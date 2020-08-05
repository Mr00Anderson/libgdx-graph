package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.renderer.PropertyType;

public interface GraphBoxOutputConnector {
    enum Side {
        Right, Bottom;
    }

    String getId();

    Side getSide();

    PropertyType getPropertyType();

    float getOffset();
}
