package com.gempukku.libgdx.graph.renderer.loader;

public interface PipelineNodeConfiguration {
    Iterable<PipelineNodeInput> getNodeInputs();

    Iterable<PipelineNodeOutput> getNodeOutputs();

    PipelineNodeInput getNodeInput(String name);

    PipelineNodeOutput getNodeOutput(String name);
}
