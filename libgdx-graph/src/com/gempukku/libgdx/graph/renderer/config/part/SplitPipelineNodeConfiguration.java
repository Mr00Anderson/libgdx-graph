package com.gempukku.libgdx.graph.renderer.config.part;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class SplitPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public SplitPipelineNodeConfiguration() {
        super("Split", "Split");
        addNodeInput(
                new PipelineNodeInputImpl("input", "Input", true, PropertyType.Color, PropertyType.Vector3, PropertyType.Vector2));
        addNodeOutput(
                new PipelineNodeOutputImpl("x", "X", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("y", "Y", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("z", "Z", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("w", "W", PropertyType.Vector1));
    }
}
