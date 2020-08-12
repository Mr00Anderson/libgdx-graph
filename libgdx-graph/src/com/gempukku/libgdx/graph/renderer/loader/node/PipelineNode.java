package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.google.common.base.Function;

public interface PipelineNode {
    PipelineNodeConfiguration getConfiguration();

    Function<PipelineRenderingContext, ?> getOutputSupplier(String name, PropertyType propertyType);

    void startFrame(float delta);

    void endFrame();

    void dispose();
}
