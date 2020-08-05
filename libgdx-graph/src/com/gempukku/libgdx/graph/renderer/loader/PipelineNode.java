package com.gempukku.libgdx.graph.renderer.loader;

import com.google.common.base.Supplier;

public interface PipelineNode {
    Supplier<?> getOutputSupplier(String name);

    void startFrame();

    void endFrame();

    void dispose();
}
