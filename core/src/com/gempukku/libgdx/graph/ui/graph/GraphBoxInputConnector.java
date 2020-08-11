package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInput;

public interface GraphBoxInputConnector extends PipelineNodeInput {
    enum Side {
        Left, Top;
    }

    Side getSide();

    float getOffset();
}
