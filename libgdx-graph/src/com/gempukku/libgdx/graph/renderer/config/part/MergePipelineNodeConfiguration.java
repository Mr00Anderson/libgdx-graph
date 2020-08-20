package com.gempukku.libgdx.graph.renderer.config.part;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Vector2;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Vector3;

public class MergePipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public MergePipelineNodeConfiguration() {
        super("Merge", "Merge");
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
