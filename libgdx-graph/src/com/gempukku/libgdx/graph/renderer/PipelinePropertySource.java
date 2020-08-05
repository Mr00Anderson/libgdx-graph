package com.gempukku.libgdx.graph.renderer;

public interface PipelinePropertySource {
    PipelineProperty getPipelineProperty(String property);

    Iterable<? extends PipelineProperty> getProperties();
}
