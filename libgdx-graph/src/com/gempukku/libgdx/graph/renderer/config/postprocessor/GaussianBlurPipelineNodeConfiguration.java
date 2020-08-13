package com.gempukku.libgdx.graph.renderer.config.postprocessor;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class GaussianBlurPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public GaussianBlurPipelineNodeConfiguration() {
        super("GaussianBlur", "Gaussian blur");
        addNodeInput(
                new PipelineNodeInputImpl("blurRadius", "Radius", PropertyType.Float));
        addNodeInput(
                new PipelineNodeInputImpl("input", "Input", true, true, PropertyType.RenderPipeline));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Output", true, PropertyType.RenderPipeline));
    }
}
