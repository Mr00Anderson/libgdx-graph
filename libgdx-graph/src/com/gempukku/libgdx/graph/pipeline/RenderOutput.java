package com.gempukku.libgdx.graph.pipeline;

public interface RenderOutput {
    int getRenderWidth();

    int getRenderHeight();

    void output(RenderPipeline renderPipeline);
}
