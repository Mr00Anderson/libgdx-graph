package com.gempukku.libgdx.graph.renderer.config.provided;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Float;

public class TimePipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public TimePipelineNodeConfiguration() {
        super("Time", "Time");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("time", "Time", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("sinTime", "sin(Time)", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("cosTime", "cos(Time)", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("deltaTime", "deltaTime", Float));
    }
}
