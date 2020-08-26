package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Boolean;

public class ValueBooleanPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ValueBooleanPipelineNodeConfiguration() {
        super("ValueBoolean", "Boolean", "Constant");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Boolean));
    }
}
