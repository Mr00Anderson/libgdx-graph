package com.gempukku.libgdx.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.graph.data.GraphProperty;

import java.util.Map;

public interface NodeConfiguration<T extends FieldType> {
    String getType();

    String getName();

    String getMenuLocation();

    Map<String, GraphNodeInput<T>> getNodeInputs();

    Map<String, GraphNodeOutput<T>> getNodeOutputs();

    boolean isValid(Map<String, ? extends GraphNodeOutput<T>> inputs, Iterable<? extends GraphProperty<T>> properties);
}
