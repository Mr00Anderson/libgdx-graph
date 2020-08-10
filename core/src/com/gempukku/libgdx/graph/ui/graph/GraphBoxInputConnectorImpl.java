package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInput;
import com.google.common.base.Supplier;

public class GraphBoxInputConnectorImpl implements GraphBoxInputConnector {
    private Side side;
    private Supplier<Float> offsetSupplier;
    private PipelineNodeInput pipelineNodeInput;

    public GraphBoxInputConnectorImpl(Side side, Supplier<Float> offsetSupplier, PipelineNodeInput pipelineNodeInput) {
        this.side = side;
        this.offsetSupplier = offsetSupplier;
        this.pipelineNodeInput = pipelineNodeInput;
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public float getOffset() {
        return offsetSupplier.get();
    }

    @Override
    public PipelineNodeInput getPipelineNodeInput() {
        return pipelineNodeInput;
    }
}
