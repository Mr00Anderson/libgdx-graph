package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.RenderPipeline;

public class EndPipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public EndPipelineNodeConfiguration() {
        super("PipelineEnd", "Pipeline end");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
    }
}
