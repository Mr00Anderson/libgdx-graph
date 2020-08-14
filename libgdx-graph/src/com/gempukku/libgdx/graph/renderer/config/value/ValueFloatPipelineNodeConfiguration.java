package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class ValueFloatPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public ValueFloatPipelineNodeConfiguration() {
        super("ValueFloat", "Float value");
        addNodeOutput(
                new PipelineNodeOutputImpl("value", "Value", PropertyType.Float));
    }
}
