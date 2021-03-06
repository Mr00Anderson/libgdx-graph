package com.gempukku.libgdx.graph.pipeline.property;

import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class GraphModelsPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.GraphModels;
    }

    @Override
    public WritablePipelineProperty createProperty(JSONObject data) {
        return new WritablePipelineProperty(PipelineFieldType.GraphModels,
                new Supplier<GraphShaderModels>() {
                    @Override
                    public GraphShaderModels get() {
                        return null;
                    }
                });
    }
}
