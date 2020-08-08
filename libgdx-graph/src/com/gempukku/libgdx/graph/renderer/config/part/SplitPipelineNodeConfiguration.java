package com.gempukku.libgdx.graph.renderer.config.part;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;
import com.google.common.base.Predicates;

import java.util.Arrays;

public class SplitPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public SplitPipelineNodeConfiguration() {
        super("Split");
        addNodeInput(
                new PipelineNodeInputImpl(true, "input", Predicates.in(Arrays.asList(PropertyType.Vector2, PropertyType.Vector3, PropertyType.Color))));
        addNodeOutput(
                new PipelineNodeOutputImpl("x", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("y", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("z", PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("w", PropertyType.Vector1));
    }
}
