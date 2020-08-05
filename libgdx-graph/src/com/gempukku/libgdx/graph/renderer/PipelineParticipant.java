package com.gempukku.libgdx.graph.renderer;

public interface PipelineParticipant {
    void renderToPipeline(RenderPipeline renderPipeline, PipelinePropertySource pipelinePropertySource);
}
