package com.gempukku.libgdx.graph.graph.pipeline;

public interface PipelinePropertySource {
    PipelineProperty getPipelineProperty(String property);

    boolean hasPipelineProperty(String property);

    Iterable<? extends PipelineProperty> getProperties();
}
