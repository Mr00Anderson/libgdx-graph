package com.gempukku.libgdx.graph.renderer.loader.rendering.node;

import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.config.rendering.EndPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;

public class EndPipelineNode implements PipelineNode {
    private PipelineNodeConfiguration configuration = new EndPipelineNodeConfiguration();
    private PipelineNode.FieldOutput<RenderPipeline> renderPipeline;

    public EndPipelineNode(PipelineNode.FieldOutput<RenderPipeline> renderPipeline) {
        this.renderPipeline = renderPipeline;
    }

    @Override
    public FieldOutput<?> getFieldOutput(String name) {
        return null;
    }

    @Override
    public void startFrame(float delta) {

    }

    @Override
    public void endFrame() {
    }

    @Override
    public void dispose() {

    }

    public RenderPipeline executePipeline(PipelineRenderingContext pipelineRenderingContext) {
        return renderPipeline.getValue(pipelineRenderingContext);
    }
}
