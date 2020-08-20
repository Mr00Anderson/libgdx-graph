package com.gempukku.libgdx.graph.graph.pipeline.config.value;

import com.gempukku.libgdx.graph.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Color;

public class ValueColorPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public ValueColorPipelineNodeConfiguration() {
        super("ValueColor", "Color value");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Color));
    }
}
