package com.gempukku.libgdx.graph.data;

public interface GraphNode<T extends FieldType> {
    boolean isInputField(String fieldId);

    GraphNodeInput<T> getInput(String fieldId);

    GraphNodeOutput<T> getOutput(String fieldId);

    Iterable<? extends GraphNodeInput<T>> getInputs();

    Iterable<? extends GraphNodeOutput<T>> getOutputs();
}
