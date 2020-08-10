package com.gempukku.libgdx.graph.renderer.config.postprocessor;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

import java.util.Arrays;

public class GammaCorrectionPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public GammaCorrectionPipelineNodeConfiguration() {
        super("GammaCorrection", "Gamma correction");
        addNodeInput(
                new PipelineNodeInputImpl("gamma", "Gamma", PropertyType.Vector1));
        addNodeInput(
                new PipelineNodeInputImpl("input", "Input", Arrays.asList(PropertyType.RenderPipeline), true, true));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Output", Arrays.asList(PropertyType.RenderPipeline), true));
    }
}
