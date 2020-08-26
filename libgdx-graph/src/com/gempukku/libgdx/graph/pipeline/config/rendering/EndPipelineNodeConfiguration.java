package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.RenderPipeline;

public class EndPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public EndPipelineNodeConfiguration() {
        super("PipelineEnd", "Pipeline end", null);
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
    }
}
