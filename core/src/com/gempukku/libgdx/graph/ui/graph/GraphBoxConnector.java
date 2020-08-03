package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.graph.pipeline.PropertyType;

public interface GraphBoxConnector {
    enum Side {
        Left, Right, Top, Bottom;
    }

    enum CommunicationType {
        Input, Output;
    }

    String getId();

    Side getSide();

    CommunicationType getCommunicationType();

    PropertyType getPropertyType();

    float getOffset();
}
