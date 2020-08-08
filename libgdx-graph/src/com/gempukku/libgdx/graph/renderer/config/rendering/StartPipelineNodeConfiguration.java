package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;
import com.google.common.base.Predicates;

public class StartPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public StartPipelineNodeConfiguration() {
        super("StartPipeline");
        addNodeInput(
                new PipelineNodeInputImpl(false, "background", Predicates.equalTo(PropertyType.Color)));
        addNodeInput(
                new PipelineNodeInputImpl(false, "size", Predicates.equalTo(PropertyType.Vector2)));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", PropertyType.RenderPipeline));
    }
}
