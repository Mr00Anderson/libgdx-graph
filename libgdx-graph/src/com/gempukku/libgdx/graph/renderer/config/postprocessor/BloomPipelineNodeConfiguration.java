package com.gempukku.libgdx.graph.renderer.config.postprocessor;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

import java.util.Arrays;

public class BloomPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public BloomPipelineNodeConfiguration() {
        super("Bloom", "Bloom post-processor");
        addNodeInput(
                new PipelineNodeInputImpl("bloomRadius", "Radius", PropertyType.Vector1));
        addNodeInput(
                new PipelineNodeInputImpl("minimalBrightness", "Min Brightness", PropertyType.Vector1));
        addNodeInput(
                new PipelineNodeInputImpl("bloomStrength", "Strength", PropertyType.Vector1));
        addNodeInput(
                new PipelineNodeInputImpl("input", "Input", Arrays.asList(PropertyType.RenderPipeline), true, true));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Output", Arrays.asList(PropertyType.RenderPipeline), true));
    }
}
