package com.gempukku.libgdx.graph.renderer.config.part;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class MergePipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public MergePipelineNodeConfiguration() {
        super("Merge", "Merge");
        addNodeInput(
                new PipelineNodeInputImpl("x", "X", PropertyType.Float));
        addNodeInput(
                new PipelineNodeInputImpl("y", "Y", PropertyType.Float));
        addNodeInput(
                new PipelineNodeInputImpl("z", "Z", PropertyType.Float));
        addNodeInput(
                new PipelineNodeInputImpl("w", "W", PropertyType.Float));
        addNodeOutput(
                new PipelineNodeOutputImpl("v2", "Vector2", PropertyType.Vector2));
        addNodeOutput(
                new PipelineNodeOutputImpl("v3", "Vector3", PropertyType.Vector3));
        addNodeOutput(
                new PipelineNodeOutputImpl("color", "Color", PropertyType.Color));
    }
}
