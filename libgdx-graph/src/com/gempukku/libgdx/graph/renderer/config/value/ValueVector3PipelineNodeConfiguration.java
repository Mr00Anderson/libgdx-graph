package com.gempukku.libgdx.graph.renderer.config.value;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Vector3;

public class ValueVector3PipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public ValueVector3PipelineNodeConfiguration() {
        super("ValueVector3", "Vector3 value");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("value", "Value", Vector3));
    }
}
