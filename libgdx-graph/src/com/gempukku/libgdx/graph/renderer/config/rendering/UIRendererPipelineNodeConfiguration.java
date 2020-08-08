package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;
import com.google.common.base.Predicates;

public class UIRendererPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public UIRendererPipelineNodeConfiguration() {
        super("UIRenderer");
        addNodeInput(
                new PipelineNodeInputImpl(true, "input",
                        Predicates.equalTo(PropertyType.RenderPipeline)));
        addNodeInput(
                new PipelineNodeInputImpl(false, "stage",
                        Predicates.equalTo(PropertyType.Stage)));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", PropertyType.RenderPipeline));
    }
}
