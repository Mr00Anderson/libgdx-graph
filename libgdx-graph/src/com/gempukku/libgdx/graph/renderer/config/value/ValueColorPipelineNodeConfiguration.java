package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Color;

public class ValueColorPipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public ValueColorPipelineNodeConfiguration() {
        super("ValueColor", "Color value");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Color));
    }
}
