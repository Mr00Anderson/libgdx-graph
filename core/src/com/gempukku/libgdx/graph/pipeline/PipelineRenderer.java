package com.gempukku.libgdx.graph.pipeline;

public interface PipelineRenderer extends PipelinePropertySource {
    void setPipelineProperty(String property, Object value);

    void resize(int width, int height);

    void render();

    void dispose();
}
