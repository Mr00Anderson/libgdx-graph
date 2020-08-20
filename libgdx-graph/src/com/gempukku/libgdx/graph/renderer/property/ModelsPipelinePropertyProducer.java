package com.gempukku.libgdx.graph.renderer.property;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.PipelineRendererModels;
import com.gempukku.libgdx.graph.renderer.impl.WritablePipelineProperty;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class ModelsPipelinePropertyProducer implements PipelinePropertyProducer<PipelineFieldType> {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Models");
    }

    @Override
    public WritablePipelineProperty<PipelineFieldType> createProperty(JSONObject data) {
        return new WritablePipelineProperty<PipelineFieldType>(PipelineFieldType.Models,
                new Supplier<PipelineRendererModels>() {
                    @Override
                    public PipelineRendererModels get() {
                        return null;
                    }
                });
    }
}
