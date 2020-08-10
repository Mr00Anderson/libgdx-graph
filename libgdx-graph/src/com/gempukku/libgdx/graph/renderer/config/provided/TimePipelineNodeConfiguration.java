package com.gempukku.libgdx.graph.renderer.config.provided;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class TimePipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public TimePipelineNodeConfiguration() {
        super("Time", "Time");
        addNodeOutput(
                new PipelineNodeOutputImpl("time", "Time", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("sinTime", "sin(Time)", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("cosTime", "cos(Time)", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("deltaTime", "deltaTime", PropertyType.Vector1));
    }
}
