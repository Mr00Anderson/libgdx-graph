package com.gempukku.graph.pipeline;

public interface PipelinePropertySource {
    PipelineProperty getPipelineProperty(String property);

    Iterable<? extends PipelineProperty> getProperties();
}
