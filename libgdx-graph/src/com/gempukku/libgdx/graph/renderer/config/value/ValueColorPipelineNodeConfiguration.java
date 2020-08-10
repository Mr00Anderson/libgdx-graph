package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class ValueColorPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public ValueColorPipelineNodeConfiguration() {
        super("ValueColor", "Color value");
        addNodeOutput(
                new PipelineNodeOutputImpl("value", "Value", PropertyType.Color));
    }
}
