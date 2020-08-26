package com.gempukku.libgdx.graph.pipeline.config.part;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Vector2;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Vector3;

public class MergePipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public MergePipelineNodeConfiguration() {
        super("Merge", "Merge", "Math");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("x", "X", Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("y", "Y", Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("z", "Z", Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("w", "W", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("v2", "Vector2", Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("v3", "Vector3", Vector3));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("color", "Color", Color));
    }
}
