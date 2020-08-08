package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;
import com.google.common.base.Predicates;

public class DefaultRendererPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public DefaultRendererPipelineNodeConfiguration() {
        super("DefaultRenderer");
        addNodeInput(
                new PipelineNodeInputImpl(false, "models",
                        Predicates.equalTo(PropertyType.Models)));
        addNodeInput(
                new PipelineNodeInputImpl(false, "lights",
                        Predicates.equalTo(PropertyType.Lights)));
        addNodeInput(
                new PipelineNodeInputImpl(false, "camera",
                        Predicates.equalTo(PropertyType.Camera)));
        addNodeInput(
                new PipelineNodeInputImpl(true, "input",
                        Predicates.equalTo(PropertyType.RenderPipeline)));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", PropertyType.RenderPipeline));
    }
}
