package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class ValueBooleanPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public ValueBooleanPipelineNodeConfiguration() {
        super("ValueBoolean");
        addNodeOutput(
                new PipelineNodeOutputImpl("value", PropertyType.Boolean));
    }
}
