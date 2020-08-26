package com.gempukku.libgdx.graph.pipeline.config.postprocessor;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.RenderPipeline;

public class BloomPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public BloomPipelineNodeConfiguration() {
        super("Bloom", "Bloom post-processor", "Post-processing");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("bloomRadius", "Radius", Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("minimalBrightness", "Min Brightness", Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("bloomStrength", "Strength", Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
