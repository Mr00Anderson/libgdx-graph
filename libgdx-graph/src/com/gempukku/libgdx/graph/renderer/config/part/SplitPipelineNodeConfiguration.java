package com.gempukku.libgdx.graph.renderer.config.part;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Vector2;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Vector3;

public class SplitPipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
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
