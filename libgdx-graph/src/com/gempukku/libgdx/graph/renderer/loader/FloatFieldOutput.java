package com.gempukku.libgdx.graph.renderer.loader;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;

public class FloatFieldOutput implements PipelineNode.FieldOutput<Float> {
    private float value;

    public FloatFieldOutput(float value) {
        this.value = value;
    }

    @Override
    public PropertyType getPropertyType() {
        return PropertyType.Float;
    }

    @Override
    public Float getValue(PipelineRenderingContext context) {
        return value;
    }
}
