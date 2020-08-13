package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class DefaultRendererPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public DefaultRendererPipelineNodeConfiguration() {
        super("DefaultRenderer", "Default renderer");
        addNodeInput(
                new PipelineNodeInputImpl("models", "Models", true, PropertyType.Models));
        addNodeInput(
                new PipelineNodeInputImpl("camera", "Camera", true, PropertyType.Camera));
        addNodeInput(
                new PipelineNodeInputImpl("lights", "Lights", PropertyType.Lights));
        addNodeInput(
                new PipelineNodeInputImpl("input", "Input", true, true, PropertyType.RenderPipeline));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Output", true, PropertyType.RenderPipeline));
    }
}
