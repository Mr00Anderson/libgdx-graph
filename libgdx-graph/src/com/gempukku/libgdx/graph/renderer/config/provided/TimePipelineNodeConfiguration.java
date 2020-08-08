package com.gempukku.libgdx.graph.renderer.config.provided;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class TimePipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public TimePipelineNodeConfiguration() {
        super("Time");
        addNodeOutput(
                new PipelineNodeOutputImpl("time", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("sinTime", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("cosTime", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("deltaTime", PropertyType.Vector1));
    }
}
