package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class ValueVector2PipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public ValueVector2PipelineNodeConfiguration() {
        super("ValueVector2");
        addNodeOutput(
                new PipelineNodeOutputImpl("value", PropertyType.Vector2));
    }
}
