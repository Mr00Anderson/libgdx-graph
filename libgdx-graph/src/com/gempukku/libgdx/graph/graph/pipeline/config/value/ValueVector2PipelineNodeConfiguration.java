package com.gempukku.libgdx.graph.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Vector2;

public class ValueVector2PipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ValueVector2PipelineNodeConfiguration() {
        super("ValueVector2", "Vector2 value");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Vector2));
    }
}
