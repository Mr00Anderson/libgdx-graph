package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;

public interface PipelineNode {
    FieldOutput<?> getFieldOutput(String name);

    void startFrame(float delta);

    void endFrame();

    void dispose();

    interface FieldOutput<T> {
        FieldType getPropertyType();

        T getValue(PipelineRenderingContext context);
    }
}
