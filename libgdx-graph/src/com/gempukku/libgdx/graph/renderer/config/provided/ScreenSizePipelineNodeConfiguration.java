package com.gempukku.libgdx.graph.renderer.config.provided;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class ScreenSizePipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public ScreenSizePipelineNodeConfiguration() {
        super("ScreenSize");
        addNodeOutput(
                new PipelineNodeOutputImpl("size", PropertyType.Vector2));
        addNodeOutput(
                new PipelineNodeOutputImpl("width", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("height", PropertyType.Vector1));
    }
}
