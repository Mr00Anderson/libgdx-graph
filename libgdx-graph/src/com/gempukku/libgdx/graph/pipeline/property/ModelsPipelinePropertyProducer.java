package com.gempukku.libgdx.graph.pipeline.property;

import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererModels;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class ModelsPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.Models;
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
