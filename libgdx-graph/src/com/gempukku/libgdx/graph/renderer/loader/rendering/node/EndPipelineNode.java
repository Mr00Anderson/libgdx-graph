package com.gempukku.libgdx.graph.renderer.loader.rendering.node;

import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNode;
import com.google.common.base.Supplier;

public class EndPipelineNode implements PipelineNode {
    private Supplier<RenderPipeline> renderPipeline;

    public EndPipelineNode(Supplier<RenderPipeline> renderPipeline) {
        this.renderPipeline = renderPipeline;
    }

    @Override
    public void startFrame() {

    }

    @Override
    public void endFrame() {
    }


    @Override
    public Supplier<?> getOutputSupplier(String name) {
        return null;
    }

    @Override
    public void dispose() {

    }

    public RenderPipeline executePipeline() {
        return renderPipeline.get();
    }
}
