package com.gempukku.libgdx.graph.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Camera;
import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Lights;
import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Models;
import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.RenderPipeline;

public class DefaultRendererPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public DefaultRendererPipelineNodeConfiguration() {
        super("DefaultRenderer", "Default renderer");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("models", "Models", true, Models));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("camera", "Camera", true, Camera));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("lights", "Lights", Lights));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
