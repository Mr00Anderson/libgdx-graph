package com.gempukku.libgdx.graph.renderer.loader.property;

import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.Map;

public class PropertyPipelineNodeProducer extends PipelineNodeProducerImpl {
    private PipelineNodeConfigurationImpl configuration;

    public PropertyPipelineNodeProducer() {
        super(new PipelineNodeConfigurationImpl("Property"));
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        final String propertyName = (String) data.get("name");
        final Function<PipelineRenderingContext, ?> result = new Function<PipelineRenderingContext, Object>() {
            @Override
            public Object apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                return pipelineRenderingContext.getPipelinePropertySource().getPipelineProperty(propertyName).getValue();
            }
        };

        return new PipelineNode() {
            @Override
            public Function<PipelineRenderingContext, ?> getOutputSupplier(String name) {
                if (!name.equals("value"))
                    throw new IllegalArgumentException();
                return result;
            }

            @Override
            public void startFrame(float delta) {

            }

            @Override
            public void endFrame() {

            }

            @Override
            public void dispose() {

            }
        };
    }
}
