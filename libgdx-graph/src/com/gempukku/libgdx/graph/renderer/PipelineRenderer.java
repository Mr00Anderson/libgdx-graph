package com.gempukku.libgdx.graph.renderer;

public interface PipelineRenderer extends PipelinePropertySource {
    void setPipelineProperty(String property, Object value);

    void unsetPipelineProperty(String property);

    void render(RenderOutput renderOutput);

    void dispose();
}
