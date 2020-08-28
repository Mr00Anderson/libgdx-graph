package com.gempukku.libgdx.graph.data;

import org.json.simple.JSONObject;

import java.util.Map;

public interface GraphNode<T extends FieldType> {
    String getId();

    String getType();

    JSONObject getData();

    boolean isInputField(String fieldId);

    Map<String, ? extends GraphNodeInput<T>> getInputs();

    Map<String, ? extends GraphNodeOutput<T>> getOutputs();

    boolean isValid(Map<String, GraphNodeOutput<T>> inputs, Iterable<? extends GraphProperty<T>> properties);
}
