package com.gempukku.libgdx.graph.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeOutputImpl;

public class PropertyNodeConfiguration<T extends FieldType> extends NodeConfigurationImpl<T> {
    public PropertyNodeConfiguration(String type, String name, T fieldType) {
        super(type, name);
        addNodeOutput(new GraphNodeOutputImpl<T>("value", name, fieldType));
    }
}
