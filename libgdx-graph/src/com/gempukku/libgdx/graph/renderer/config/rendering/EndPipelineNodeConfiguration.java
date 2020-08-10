package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;

import java.util.Arrays;

public class EndPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public EndPipelineNodeConfiguration() {
        super("PipelineEnd", "Pipeline end");
        addNodeInput(
                new PipelineNodeInputImpl("input", "Input", Arrays.asList(PropertyType.RenderPipeline), true, true));
    }
}
