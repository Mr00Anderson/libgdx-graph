package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

import java.util.Arrays;

public class PipelineRendererNodeConfiguration extends PipelineNodeConfigurationImpl {
    public PipelineRendererNodeConfiguration() {
        super("PipelineRenderer", "Pipeline renderer");
        addNodeInput(
                new PipelineNodeInputImpl("pipeline", "Pipeline", Arrays.asList(PropertyType.RenderPipeline), true, false));
        addNodeInput(
                new PipelineNodeInputImpl("position", "Position", Arrays.asList(PropertyType.Vector2), true, false));
        addNodeInput(
                new PipelineNodeInputImpl("size", "Size", Arrays.asList(PropertyType.Vector2), true, false));
        addNodeInput(
                new PipelineNodeInputImpl("input", "Input", Arrays.asList(PropertyType.RenderPipeline), true, true));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Output", PropertyType.RenderPipeline, true));
    }
}
