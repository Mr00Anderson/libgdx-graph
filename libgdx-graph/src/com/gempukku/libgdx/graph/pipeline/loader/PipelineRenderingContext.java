package com.gempukku.libgdx.graph.pipeline.loader;

import com.gempukku.libgdx.graph.TimeProvider;
import com.gempukku.libgdx.graph.pipeline.PipelinePropertySource;

public interface PipelineRenderingContext {
    int getRenderWidth();

    int getRenderHeight();

    PipelinePropertySource getPipelinePropertySource();

    TimeProvider getTimeProvider();
}
