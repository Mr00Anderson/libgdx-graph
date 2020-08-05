package com.gempukku.libgdx.graph.renderer.loader;

import java.util.LinkedHashMap;
import java.util.Map;

public class PipelineNodeConfigurationImpl implements PipelineNodeConfiguration {
    private Map<String, PipelineNodeInput> nodeInputs = new LinkedHashMap<>();
    private Map<String, PipelineNodeOutput> nodeOutputs = new LinkedHashMap<>();

    public void addNodeInput(PipelineNodeInput input) {
        nodeInputs.put(input.getName(), input);
    }

    public void addNodeOutput(PipelineNodeOutput output) {
        nodeOutputs.put(output.getName(), output);
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
