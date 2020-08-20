package com.gempukku.libgdx.graph.renderer.config.postprocessor;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.RenderPipeline;

public class GammaCorrectionPipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public GammaCorrectionPipelineNodeConfiguration() {
        super("GammaCorrection", "Gamma correction");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("gamma", "Gamma", Float));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
