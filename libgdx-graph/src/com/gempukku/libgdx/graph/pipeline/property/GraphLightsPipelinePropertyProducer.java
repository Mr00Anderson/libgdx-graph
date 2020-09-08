package com.gempukku.libgdx.graph.pipeline.property;

import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

public class GraphLightsPipelinePropertyProducer implements PipelinePropertyProducer {
    @Override
    public PipelineFieldType getType() {
        return PipelineFieldType.GraphLights;
    }

    @Override
    public WritablePipelineProperty createProperty(JSONObject data) {
        return new WritablePipelineProperty(PipelineFieldType.GraphLights,
                new Supplier<GraphShaderEnvironment>() {
                    @Override
                    public GraphShaderEnvironment get() {
                        return null;
                    }
                });
    }
}
