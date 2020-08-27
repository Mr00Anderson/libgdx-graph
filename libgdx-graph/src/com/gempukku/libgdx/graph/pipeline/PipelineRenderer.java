package com.gempukku.libgdx.graph.pipeline;

import com.gempukku.libgdx.graph.TimeKeeper;

public interface PipelineRenderer extends PipelinePropertySource {
    void setPipelineProperty(String property, Object value);

    void unsetPipelineProperty(String property);

    void setTimeKeeper(TimeKeeper timeKeeper);

    void render(float delta, RenderOutput renderOutput);

    void dispose();
}
