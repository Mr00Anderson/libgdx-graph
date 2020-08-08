package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class ValueVector1PipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public ValueVector1PipelineNodeConfiguration() {
        super("ValueVector1");
        addNodeOutput(
                new PipelineNodeOutputImpl("value", PropertyType.Vector1));
    }
}
