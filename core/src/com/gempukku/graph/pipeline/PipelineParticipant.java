package com.gempukku.graph.pipeline;

public interface PipelineParticipant {
    void renderToPipeline(RenderPipeline renderPipeline, PipelinePropertySource pipelinePropertySource);
}
