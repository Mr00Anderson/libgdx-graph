package com.gempukku.libgdx.graph.renderer.loader.node;

import java.util.LinkedHashMap;
import java.util.Map;

public class PipelineNodeConfigurationImpl implements PipelineNodeConfiguration {
    private String type;
    private Map<String, PipelineNodeInput> nodeInputs = new LinkedHashMap<>();
    private Map<String, PipelineNodeOutput> nodeOutputs = new LinkedHashMap<>();

    public PipelineNodeConfigurationImpl(String type) {
        this.type = type;
    }

    public void addNodeInput(PipelineNodeInput input) {
        nodeInputs.put(input.getName(), input);
    }

    public void addNodeOutput(PipelineNodeOutput output) {
        nodeOutputs.put(output.getName(), output);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Iterable<PipelineNodeInput> getNodeInputs() {
        return nodeInputs.values();
    }

    @Override
    public Iterable<PipelineNodeOutput> getNodeOutputs() {
        return nodeOutputs.values();
    }

    @Override
    public PipelineNodeInput getNodeInput(String name) {
        return nodeInputs.get(name);
    }

    @Override
    public PipelineNodeOutput getNodeOutput(String name) {
        return nodeOutputs.get(name);
    }
}
