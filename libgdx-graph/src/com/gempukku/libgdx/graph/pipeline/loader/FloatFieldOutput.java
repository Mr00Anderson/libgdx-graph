package com.gempukku.libgdx.graph.pipeline.loader;

import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;

public class FloatFieldOutput implements PipelineNode.FieldOutput<Float> {
    private float value;

    public FloatFieldOutput(float value) {
        this.value = value;
    }

    @Override
    public PipelineFieldType getPropertyType() {
        return PipelineFieldType.Float;
    }

    @Override
    public Float getValue(PipelineRenderingContext context) {
        return value;
    }
}
