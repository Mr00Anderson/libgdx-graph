package com.gempukku.libgdx.graph.renderer.config.postprocessor;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;
import com.google.common.base.Predicates;

public class BloomPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public BloomPipelineNodeConfiguration() {
        super("Bloom");
        addNodeInput(
                new PipelineNodeInputImpl(false, "bloomRadius",
                        Predicates.equalTo(PropertyType.Vector1)));
        addNodeInput(
                new PipelineNodeInputImpl(false, "minimalBrightness",
                        Predicates.equalTo(PropertyType.Vector1)));
        addNodeInput(
                new PipelineNodeInputImpl(false, "bloomStrength",
                        Predicates.equalTo(PropertyType.Vector1)));
        addNodeInput(
                new PipelineNodeInputImpl(true, "input",
                        Predicates.equalTo(PropertyType.RenderPipeline)));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", PropertyType.RenderPipeline));
    }
}
