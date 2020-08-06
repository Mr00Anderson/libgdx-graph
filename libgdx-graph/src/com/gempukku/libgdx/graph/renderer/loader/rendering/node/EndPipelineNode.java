package com.gempukku.libgdx.graph.renderer.loader.rendering.node;

import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.google.common.base.Function;

public class EndPipelineNode implements PipelineNode {
    private Function<PipelineRenderingContext, RenderPipeline> renderPipeline;

    public EndPipelineNode(Function<PipelineRenderingContext, RenderPipeline> renderPipeline) {
        this.renderPipeline = renderPipeline;
    }

    @Override
    public void startFrame(float delta) {

    }

    @Override
    public void endFrame() {
    }

    @Override
    public Function<PipelineRenderingContext, ?> getOutputSupplier(String name) {
        return null;
    }

    @Override
    public void dispose() {

    }

    public RenderPipeline executePipeline(PipelineRenderingContext pipelineRenderingContext) {
        return renderPipeline.apply(pipelineRenderingContext);
    }
}
