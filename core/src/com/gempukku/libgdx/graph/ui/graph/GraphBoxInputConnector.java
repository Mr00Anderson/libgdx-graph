package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.renderer.PropertyType;

public interface GraphBoxInputConnector {
    enum Side {
        Left, Top;
    }

    String getId();

    Side getSide();

    boolean accepts(PropertyType propertyType);

    float getOffset();
}
