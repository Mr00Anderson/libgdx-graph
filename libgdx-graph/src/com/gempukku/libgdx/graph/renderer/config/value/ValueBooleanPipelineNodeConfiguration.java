package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Boolean;

public class ValueBooleanPipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public ValueBooleanPipelineNodeConfiguration() {
        super("ValueBoolean", "Boolean value");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Boolean));
    }
}
