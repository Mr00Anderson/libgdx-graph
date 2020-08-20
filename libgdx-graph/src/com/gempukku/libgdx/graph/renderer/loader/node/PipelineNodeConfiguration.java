package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;

public interface PipelineNodeConfiguration<T extends FieldType> {
    String getType();

    String getName();

    Iterable<GraphNodeInput<T>> getNodeInputs();

    Iterable<GraphNodeOutput<T>> getNodeOutputs();

    GraphNodeInput<T> getNodeInput(String name);

    GraphNodeOutput<T> getNodeOutput(String name);
}
