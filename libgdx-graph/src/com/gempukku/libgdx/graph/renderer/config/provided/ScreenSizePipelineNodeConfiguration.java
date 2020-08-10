package com.gempukku.libgdx.graph.renderer.config.provided;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class ScreenSizePipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public ScreenSizePipelineNodeConfiguration() {
        super("ScreenSize", "Screen size");
        addNodeOutput(
                new PipelineNodeOutputImpl("size", "Size", PropertyType.Vector2));
        addNodeOutput(
                new PipelineNodeOutputImpl("width", "Width", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("height", "Height", PropertyType.Vector1));
    }
}
