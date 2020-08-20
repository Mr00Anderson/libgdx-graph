package com.gempukku.libgdx.graph.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeInputImpl;

import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.RenderPipeline;

public class EndPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public EndPipelineNodeConfiguration() {
        super("PipelineEnd", "Pipeline end");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
    }
}
