package com.gempukku.libgdx.graph.graph.pipeline.property;

import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineRendererModels;
import com.gempukku.libgdx.graph.graph.pipeline.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class ModelsPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Models");
    }

    @Override
    public WritablePipelineProperty createProperty(JSONObject data) {
        return new WritablePipelineProperty(PipelineFieldType.Models,
                new Supplier<PipelineRendererModels>() {
                    @Override
                    public PipelineRendererModels get() {
                        return null;
                    }
                });
    }
}
