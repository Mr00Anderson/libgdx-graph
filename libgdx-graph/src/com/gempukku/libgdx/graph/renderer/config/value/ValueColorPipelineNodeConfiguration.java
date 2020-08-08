package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class ValueColorPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public ValueColorPipelineNodeConfiguration() {
        super("ValueVector3");
        addNodeOutput(
                new PipelineNodeOutputImpl("value", PropertyType.Color));
    }
}
