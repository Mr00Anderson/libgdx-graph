package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.RenderPipeline;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Vector2;

public class PipelineRendererNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public PipelineRendererNodeConfiguration() {
        super("PipelineRenderer", "Pipeline renderer", "Pipeline");
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
