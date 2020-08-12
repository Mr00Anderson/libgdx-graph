package com.gempukku.libgdx.graph.renderer.loader;

import com.gempukku.libgdx.graph.renderer.PipelinePropertySource;

public interface PipelineRenderingContext {
    int getRenderWidth();

    int getRenderHeight();

    PipelinePropertySource getPipelinePropertySource();
}
