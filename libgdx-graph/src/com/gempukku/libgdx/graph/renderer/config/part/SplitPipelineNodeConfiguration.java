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
                new PipelineNodeOutputImpl("x", "X", PropertyType.Float));
        addNodeOutput(
                new PipelineNodeOutputImpl("y", "Y", PropertyType.Float));
        addNodeOutput(
                new PipelineNodeOutputImpl("z", "Z", PropertyType.Float));
        addNodeOutput(
                new PipelineNodeOutputImpl("w", "W", PropertyType.Float));
    }
}
