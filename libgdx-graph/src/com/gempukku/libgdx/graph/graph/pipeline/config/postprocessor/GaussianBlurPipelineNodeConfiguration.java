package com.gempukku.libgdx.graph.graph.pipeline.config.postprocessor;

import com.gempukku.libgdx.graph.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.RenderPipeline;

public class GaussianBlurPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public GaussianBlurPipelineNodeConfiguration() {
        super("GaussianBlur", "Gaussian blur");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("blurRadius", "Radius", Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
