package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

import java.util.Arrays;

public class UIRendererPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public UIRendererPipelineNodeConfiguration() {
        super("UIRenderer", "UI renderer");
        addNodeInput(
                new PipelineNodeInputImpl("input", "Input", Arrays.asList(PropertyType.RenderPipeline), true, true));
        addNodeInput(
                new PipelineNodeInputImpl("stage", "Stage", Arrays.asList(PropertyType.Stage), true, false));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Output", PropertyType.RenderPipeline, true));
    }
}
