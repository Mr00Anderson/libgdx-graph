package com.gempukku.libgdx.graph.renderer.loader.property;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.config.PropertyPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducer;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class PropertyPipelineNodeProducer implements PipelineNodeProducer {
    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public PipelineNodeConfiguration getConfiguration(JSONObject data) {
        final PropertyType propertyType = PropertyType.valueOf((String) data.get("type"));
        return new PropertyPipelineNodeConfiguration("Property", "Property", propertyType);
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
            public PropertyType determinePropertyType(String name, List<PropertyType> acceptedPropertyTypes) {
                return propertyType;
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
