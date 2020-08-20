package com.gempukku.libgdx.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Float;

public class ValueFloatPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ValueFloatPipelineNodeConfiguration() {
        super("ValueFloat", "Float value");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Float));
    }
}
