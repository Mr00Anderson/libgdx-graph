package com.gempukku.libgdx.graph.renderer;

public interface RenderOutput {
    int getRenderWidth();

    int getRenderHeight();

    void output(RenderPipeline renderPipeline);
}
