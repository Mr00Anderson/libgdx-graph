package com.gempukku.libgdx.graph.pipeline.config.provided;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Vector2;

public class RenderSizePipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public RenderSizePipelineNodeConfiguration() {
        super("RenderSize", "Render size", "Provided");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("size", "Size", Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("width", "Width", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("height", "Height", Float));
    }
}
