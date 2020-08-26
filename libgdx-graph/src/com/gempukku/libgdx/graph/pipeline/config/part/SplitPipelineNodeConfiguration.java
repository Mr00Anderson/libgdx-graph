package com.gempukku.libgdx.graph.pipeline.config.part;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Vector2;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Vector3;

public class SplitPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public SplitPipelineNodeConfiguration() {
        super("Split", "Split", "Math");
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
