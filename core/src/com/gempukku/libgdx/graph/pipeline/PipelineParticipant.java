package com.gempukku.libgdx.graph.pipeline;

public interface PipelineParticipant {
    void renderToPipeline(RenderPipeline renderPipeline, PipelinePropertySource pipelinePropertySource);
}
