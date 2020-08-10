package com.gempukku.libgdx.graph.renderer.loader.node;

public interface PipelineNodeConfiguration {
    String getType();

    String getName();

    Iterable<PipelineNodeInput> getNodeInputs();

    Iterable<PipelineNodeOutput> getNodeOutputs();

    PipelineNodeInput getNodeInput(String name);

    PipelineNodeOutput getNodeOutput(String name);
}
