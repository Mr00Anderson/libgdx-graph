package com.gempukku.libgdx.graph.renderer.loader.rendering.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.config.rendering.EndPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.google.common.base.Function;

import java.util.List;

public class EndPipelineNode implements PipelineNode {
    private PipelineNodeConfiguration configuration = new EndPipelineNodeConfiguration();
    private Function<PipelineRenderingContext, RenderPipeline> renderPipeline;

    public EndPipelineNode(Function<PipelineRenderingContext, RenderPipeline> renderPipeline) {
        this.renderPipeline = renderPipeline;
    }

    @Override
    public PropertyType determinePropertyType(String name, List<PropertyType> acceptedPropertyTypes) {
        return null;
    }

    @Override
    public void startFrame(float delta) {

    }

    @Override
    public void endFrame() {
    }

    @Override
    public Function<PipelineRenderingContext, ?> getOutputSupplier(String name, PropertyType propertyType) {
        return null;
    }

    @Override
    public void dispose() {

    }

    public RenderPipeline executePipeline(PipelineRenderingContext pipelineRenderingContext) {
        return renderPipeline.apply(pipelineRenderingContext);
    }
}
