package com.gempukku.libgdx.graph.renderer.config;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class PropertyPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public PropertyPipelineNodeConfiguration(String type, String name, PropertyType propertyType) {
        super(type, name);
        addNodeOutput(new PipelineNodeOutputImpl("value", name, propertyType));
    }
}
