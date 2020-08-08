package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.google.common.base.Predicates;

public class EndPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public EndPipelineNodeConfiguration() {
        super("PipelineEnd");
        addNodeInput(
                new PipelineNodeInputImpl(true, "input", Predicates.equalTo(PropertyType.RenderPipeline)));
    }
}
