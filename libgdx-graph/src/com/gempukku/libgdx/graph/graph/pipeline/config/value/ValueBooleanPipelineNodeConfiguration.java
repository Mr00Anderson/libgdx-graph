package com.gempukku.libgdx.graph.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Boolean;

public class ValueBooleanPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ValueBooleanPipelineNodeConfiguration() {
        super("ValueBoolean", "Boolean value");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Boolean));
    }
}
