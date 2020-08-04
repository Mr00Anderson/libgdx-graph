package com.gempukku.libgdx.graph.pipeline;

public interface PipelinePropertySource {
    PipelineProperty getPipelineProperty(String property);

    Iterable<? extends PipelineProperty> getProperties();
}
