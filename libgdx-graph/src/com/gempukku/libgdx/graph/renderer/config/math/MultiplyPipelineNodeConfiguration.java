package com.gempukku.libgdx.graph.renderer.config.math;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;

public class MultiplyPipelineNodeConfiguration extends PipelineNodeConfigurationImpl {
    public MultiplyPipelineNodeConfiguration() {
        super("Multiply", "Multiply");
        addNodeInput(
                new PipelineNodeInputImpl("inputA", "A",
                        PropertyType.Color, PropertyType.Vector3, PropertyType.Vector2, PropertyType.Vector1));
        addNodeInput(
                new PipelineNodeInputImpl("inputB", "B",
                        PropertyType.Color, PropertyType.Vector3, PropertyType.Vector2, PropertyType.Vector1));
        addNodeOutput(
                new PipelineNodeOutputImpl("output", "Result",
                        PropertyType.Vector1, PropertyType.Vector2, PropertyType.Vector3, PropertyType.Color));
    }
}
