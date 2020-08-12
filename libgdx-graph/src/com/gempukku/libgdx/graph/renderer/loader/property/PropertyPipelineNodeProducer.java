package com.gempukku.libgdx.graph.renderer.loader.property;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.config.PropertyPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducer;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.Map;

public class PropertyPipelineNodeProducer implements PipelineNodeProducer {
    @Override
    public PipelineNodeConfiguration getConfiguration() {
        return new PipelineNodeConfigurationImpl("Property", "Property");
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        final String propertyName = (String) data.get("name");
        final PropertyType propertyType = PropertyType.valueOf((String) data.get("type"));
        final Function<PipelineRenderingContext, ?> result = new Function<PipelineRenderingContext, Object>() {
            @Override
            public Object apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                return pipelineRenderingContext.getPipelinePropertySource().getPipelineProperty(propertyName).getValue();
            }
        };
        final PipelineNodeConfiguration configuration = new PropertyPipelineNodeConfiguration("Property", "Property", propertyType);

        return new PipelineNode() {
            @Override
            public Function<PipelineRenderingContext, ?> getOutputSupplier(String name, PropertyType propertyType) {
                if (!name.equals("value"))
                    throw new IllegalArgumentException();
                return result;
            }

            @Override
            public PipelineNodeConfiguration getConfiguration() {
                return configuration;
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
