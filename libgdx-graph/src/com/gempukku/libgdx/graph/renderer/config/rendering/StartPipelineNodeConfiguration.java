package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Color;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.RenderPipeline;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Vector2;

public class StartPipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public StartPipelineNodeConfiguration() {
        super("PipelineStart", "Pipeline start");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("background", "Background color", Color));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("size", "Size", Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
