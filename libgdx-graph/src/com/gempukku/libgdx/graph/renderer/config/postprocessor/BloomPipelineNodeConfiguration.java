package com.gempukku.libgdx.graph.renderer.config.postprocessor;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class BloomPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public BloomPipelineNodeConfiguration() {
        super("Bloom", "Bloom post-processor");
        addNodeInput(
                new PipelineNodeInputImpl("bloomRadius", "Radius", PropertyType.Float));
        addNodeInput(
                new PipelineNodeInputImpl("minimalBrightness", "Min Brightness", PropertyType.Float));
        addNodeInput(
                new PipelineNodeInputImpl("bloomStrength", "Strength", PropertyType.Float));
        addNodeInput(
                new PipelineNodeInputImpl("input", "Input", true, true, PropertyType.RenderPipeline));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Output", true, PropertyType.RenderPipeline));
    }
}
