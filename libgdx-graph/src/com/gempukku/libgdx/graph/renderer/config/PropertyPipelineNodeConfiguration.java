package com.gempukku.libgdx.graph.renderer.config;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

public class PropertyPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public PropertyPipelineNodeConfiguration(String type, String name, FieldType fieldType) {
        super(type, name);
        addNodeOutput(new GraphNodeOutputImpl("value", name, fieldType));
    }
}
