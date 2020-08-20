package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Float;

public class ValueFloatPipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public ValueFloatPipelineNodeConfiguration() {
        super("ValueFloat", "Float value");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Float));
    }
}
