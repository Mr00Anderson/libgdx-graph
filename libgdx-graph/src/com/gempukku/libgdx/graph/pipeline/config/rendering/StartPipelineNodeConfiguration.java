package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.RenderPipeline;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Vector2;

public class StartPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public StartPipelineNodeConfiguration() {
        super("PipelineStart", "Pipeline start", "Pipeline");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("background", "Background color", Color));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("size", "Size", Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
