package com.gempukku.libgdx.graph.renderer.loader;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;

public class FloatFieldOutput implements PipelineNode.FieldOutput<Float> {
    private float value;

    public FloatFieldOutput(float value) {
        this.value = value;
    }

    @Override
    public FieldType getPropertyType() {
        return PipelineFieldType.Float;
    }

    @Override
    public Float getValue(PipelineRenderingContext context) {
        return value;
    }
}
