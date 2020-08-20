package com.gempukku.libgdx.graph.renderer.config.provided;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Vector2;

public class RenderSizePipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public RenderSizePipelineNodeConfiguration() {
        super("RenderSize", "Render size");
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("size", "Size", Vector2));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("width", "Width", Float));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("height", "Height", Float));
    }
}
