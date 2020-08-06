package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.google.common.base.Function;

public interface PipelineNode {
    Function<PipelineRenderingContext, ?> getOutputSupplier(String name);

    void startFrame(float delta);

    void endFrame();

    void dispose();
}
