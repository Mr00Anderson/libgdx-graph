package com.gempukku.libgdx.graph.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;

public interface NodeConfiguration<T extends FieldType> {
    String getType();

    String getName();

    Iterable<GraphNodeInput<T>> getNodeInputs();

    Iterable<GraphNodeOutput<T>> getNodeOutputs();

    GraphNodeInput<T> getNodeInput(String name);

    GraphNodeOutput<T> getNodeOutput(String name);
}
