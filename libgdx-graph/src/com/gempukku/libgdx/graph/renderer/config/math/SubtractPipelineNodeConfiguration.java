package com.gempukku.libgdx.graph.renderer.config.math;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class SubtractPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public SubtractPipelineNodeConfiguration() {
        super("Subtract", "Subtract");
        addNodeInput(
                new PipelineNodeInputImpl("inputA", "A", true,
                        PropertyType.Color, PropertyType.Vector3, PropertyType.Vector2, PropertyType.Float));
        addNodeInput(
                new PipelineNodeInputImpl("inputB", "B", true,
                        PropertyType.Color, PropertyType.Vector3, PropertyType.Vector2, PropertyType.Float));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Result",
                        PropertyType.Float, PropertyType.Vector2, PropertyType.Vector3, PropertyType.Color));
    }
}
