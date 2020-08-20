package com.gempukku.libgdx.graph.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Vector3;

public class ValueVector3PipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ValueVector3PipelineNodeConfiguration() {
        super("ValueVector3", "Vector3 value");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Vector3));
    }
}
