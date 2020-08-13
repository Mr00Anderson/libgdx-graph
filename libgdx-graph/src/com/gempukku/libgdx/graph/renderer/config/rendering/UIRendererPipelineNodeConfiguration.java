package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class UIRendererPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public UIRendererPipelineNodeConfiguration() {
        super("UIRenderer", "UI renderer");
        addNodeInput(
                new PipelineNodeInputImpl("input", "Input", true, true, PropertyType.RenderPipeline));
        addNodeInput(
                new PipelineNodeInputImpl("stage", "Stage", true, PropertyType.Stage));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Output", true, PropertyType.RenderPipeline));
    }
}
