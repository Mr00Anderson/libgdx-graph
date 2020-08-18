package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class PipelineRendererNodeConfiguration extends PipelineNodeConfigurationImpl {
    public PipelineRendererNodeConfiguration() {
        super("PipelineRenderer", "Pipeline renderer");
        addNodeInput(
                new PipelineNodeInputImpl("pipeline", "Pipeline", true, PropertyType.RenderPipeline));
        addNodeInput(
                new PipelineNodeInputImpl("position", "Position", true, PropertyType.Vector2));
        addNodeInput(
                new PipelineNodeInputImpl("size", "Size", false, PropertyType.Vector2));
        addNodeInput(
                new PipelineNodeInputImpl("input", "Input", true, true, PropertyType.RenderPipeline));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Output", true, PropertyType.RenderPipeline));
    }
}
