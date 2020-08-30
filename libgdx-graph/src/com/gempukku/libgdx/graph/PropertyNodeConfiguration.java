package com.gempukku.libgdx.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import java.util.Map;

public class PropertyNodeConfiguration<T extends FieldType> extends NodeConfigurationImpl<T> {
    private String name;
    private T fieldType;

    public PropertyNodeConfiguration(String name, T fieldType) {
        super("Property", name, null);
        this.name = name;
        this.fieldType = fieldType;
        addNodeOutput(new GraphNodeOutputImpl<T>("value", name, fieldType));
    }

    @Override
    public boolean isValid(Map<String, T> inputTypes, Iterable<? extends GraphProperty<T>> properties) {
        for (GraphProperty<T> property : properties) {
            if (property.getName().equals(name) && property.getType() == fieldType)
                return true;
        }
        return false;
    }
}
