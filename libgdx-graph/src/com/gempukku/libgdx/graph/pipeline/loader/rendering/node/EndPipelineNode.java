package com.gempukku.libgdx.graph.pipeline.loader.rendering.node;

import com.gempukku.libgdx.graph.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.config.rendering.EndPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;

public class EndPipelineNode implements PipelineNode {
    private NodeConfiguration configuration = new EndPipelineNodeConfiguration();
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
