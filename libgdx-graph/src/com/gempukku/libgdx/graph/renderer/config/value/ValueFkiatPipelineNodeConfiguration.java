package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class ValueFkiatPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public ValueFkiatPipelineNodeConfiguration() {
        super("ValueFloat", "Float value");
        addNodeOutput(
                new PipelineNodeOutputImpl("value", "Value", PropertyType.Float));
    }
}
