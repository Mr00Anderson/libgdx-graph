package com.gempukku.libgdx.graph.graph.pipeline.config.postprocessor;

import com.gempukku.libgdx.graph.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.RenderPipeline;

public class BloomPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public BloomPipelineNodeConfiguration() {
        super("Bloom", "Bloom post-processor");
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
