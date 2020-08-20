package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;

import java.util.LinkedHashMap;
import java.util.Map;

public class PipelineNodeConfigurationImpl<T extends FieldType> implements PipelineNodeConfiguration<T> {
    private String type;
    private String name;
    private Map<String, GraphNodeInput<T>> nodeInputs = new LinkedHashMap<>();
    private Map<String, GraphNodeOutput<T>> nodeOutputs = new LinkedHashMap<>();

    public PipelineNodeConfigurationImpl(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public void addNodeInput(GraphNodeInput<T> input) {
        nodeInputs.put(input.getFieldId(), input);
    }

    public void addNodeOutput(GraphNodeOutput<T> output) {
        nodeOutputs.put(output.getFieldId(), output);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iterable<GraphNodeInput<T>> getNodeInputs() {
        return nodeInputs.values();
    }

    @Override
    public Iterable<GraphNodeOutput<T>> getNodeOutputs() {
        return nodeOutputs.values();
    }

    @Override
    public GraphNodeInput<T> getNodeInput(String name) {
        return nodeInputs.get(name);
    }

    @Override
    public GraphNodeOutput<T> getNodeOutput(String name) {
        return nodeOutputs.get(name);
    }
}
