package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Vector2;

public class ValueVector2PipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public ValueVector2PipelineNodeConfiguration() {
        super("ValueVector2", "Vector2 value");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Vector2));
    }
}
