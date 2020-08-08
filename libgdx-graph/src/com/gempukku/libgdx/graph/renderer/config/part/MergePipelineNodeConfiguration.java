package com.gempukku.libgdx.graph.renderer.config.part;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;
import com.google.common.base.Predicates;

public class MergePipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public MergePipelineNodeConfiguration() {
        super("Merge");
        addNodeInput(
                new PipelineNodeInputImpl(false, "x", Predicates.equalTo(PropertyType.Vector1)));
        addNodeInput(
                new PipelineNodeInputImpl(false, "y", Predicates.equalTo(PropertyType.Vector1)));
        addNodeInput(
                new PipelineNodeInputImpl(false, "z", Predicates.equalTo(PropertyType.Vector1)));
        addNodeInput(
                new PipelineNodeInputImpl(false, "w", Predicates.equalTo(PropertyType.Vector1)));
        addNodeOutput(
                new PipelineNodeOutputImpl("v2", PropertyType.Vector2));
        addNodeOutput(
                new PipelineNodeOutputImpl("v3", PropertyType.Vector3));
        addNodeOutput(
                new PipelineNodeOutputImpl("color", PropertyType.Color));
    }
}
