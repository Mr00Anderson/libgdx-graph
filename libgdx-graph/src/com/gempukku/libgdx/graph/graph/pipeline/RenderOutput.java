package com.gempukku.libgdx.graph.graph.pipeline;

public interface RenderOutput {
    int getRenderWidth();

    int getRenderHeight();

    void output(RenderPipeline renderPipeline);
}
