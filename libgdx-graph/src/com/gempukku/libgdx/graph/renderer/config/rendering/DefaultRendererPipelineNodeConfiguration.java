package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

import java.util.Arrays;

public class DefaultRendererPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public DefaultRendererPipelineNodeConfiguration() {
        super("DefaultRenderer", "Default renderer");
        addNodeInput(
                new PipelineNodeInputImpl("models", "Models", Arrays.asList(PropertyType.Models), true, false));
        addNodeInput(
                new PipelineNodeInputImpl("camera", "Camera", Arrays.asList(PropertyType.Camera), true, false));
        addNodeInput(
                new PipelineNodeInputImpl("lights", "Lights", PropertyType.Lights));
        addNodeInput(
                new PipelineNodeInputImpl("input", "Input", Arrays.asList(PropertyType.RenderPipeline), true, true));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Output", Arrays.asList(PropertyType.RenderPipeline), true));
    }
}
