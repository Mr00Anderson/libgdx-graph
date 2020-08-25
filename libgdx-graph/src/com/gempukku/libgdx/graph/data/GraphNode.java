package com.gempukku.libgdx.graph.data;

import java.util.Map;

public interface GraphNode<T extends FieldType> {
    String getId();

    boolean isInputField(String fieldId);

    Map<String, ? extends GraphNodeInput<T>> getInputs();

    Map<String, ? extends GraphNodeOutput<T>> getOutputs();
}
