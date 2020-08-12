package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class StartPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public StartPipelineNodeConfiguration() {
        super("PipelineStart", "Pipeline start");
        addNodeInput(
                new PipelineNodeInputImpl("background", "Background color", PropertyType.Color));
        addNodeInput(
                new PipelineNodeInputImpl("size", "Size", PropertyType.Vector2));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Output", PropertyType.RenderPipeline, true));
    }
}
