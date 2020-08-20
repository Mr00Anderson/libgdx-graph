package com.gempukku.libgdx.graph.graph.pipeline.config.part;

import com.gempukku.libgdx.graph.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Vector2;
import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Vector3;

public class SplitPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public SplitPipelineNodeConfiguration() {
        super("Split", "Split");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, Color, Vector3, Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("x", "X", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("y", "Y", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("z", "Z", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("w", "W", Float));
    }
}
