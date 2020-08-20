package com.gempukku.libgdx.graph.renderer.config.math;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Vector2;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Vector3;

public class SubtractPipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public SubtractPipelineNodeConfiguration() {
        super("Subtract", "Subtract");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("inputA", "A", true,
                        Color, Vector3, Vector2, Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("inputB", "B", true,
                        Color, Vector3, Vector2, Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Result",
                        Float, Vector2, Vector3, Color));
    }
}
