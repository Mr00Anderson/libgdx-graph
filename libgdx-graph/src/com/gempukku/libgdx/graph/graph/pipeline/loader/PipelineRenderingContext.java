package com.gempukku.libgdx.graph.graph.pipeline.loader;

import com.gempukku.libgdx.graph.graph.pipeline.PipelinePropertySource;

public interface PipelineRenderingContext {
    int getRenderWidth();

    int getRenderHeight();

    PipelinePropertySource getPipelinePropertySource();
}
