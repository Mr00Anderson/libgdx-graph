package com.gempukku.libgdx.graph.renderer.loader.value.node;

import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.google.common.base.Function;

import javax.annotation.Nullable;

public class ValuePipelineNode implements PipelineNode {
    private String propertyName;
    private Function<PipelineRenderingContext, Object> value;

    public ValuePipelineNode(String propertyName, final Object value) {
        this.propertyName = propertyName;
        this.value = new Function<PipelineRenderingContext, Object>() {
            @Override
            public Object apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                return value;
            }
        };
    }

    @Override
    public void startFrame(float delta) {

    }

    @Override
    public Function<PipelineRenderingContext, ?> getOutputSupplier(String name) {
        if (!name.equals(propertyName))
            throw new IllegalArgumentException();
        return value;
    }

    @Override
    public void endFrame() {

    }

    @Override
    public void dispose() {

    }
}
