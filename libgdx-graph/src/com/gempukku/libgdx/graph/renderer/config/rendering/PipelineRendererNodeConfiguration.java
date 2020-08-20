package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.RenderPipeline;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Vector2;

public class PipelineRendererNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public PipelineRendererNodeConfiguration() {
        super("PipelineRenderer", "Pipeline renderer");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("pipeline", "Pipeline", true, RenderPipeline));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("position", "Position", true, Vector2));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("size", "Size", false, Vector2));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
