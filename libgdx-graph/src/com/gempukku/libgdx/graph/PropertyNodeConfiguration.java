package com.gempukku.libgdx.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

public class PropertyNodeConfiguration<T extends FieldType> extends NodeConfigurationImpl<T> {
    public PropertyNodeConfiguration(String name, T fieldType) {
        super("Property", name, null);
        addNodeOutput(new GraphNodeOutputImpl<T>("value", name, fieldType));
    }
}
