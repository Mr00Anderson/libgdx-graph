package com.gempukku.libgdx.graph.renderer.loader.value.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.util.List;

public class ValuePipelineNode implements PipelineNode {
    private PipelineNodeConfiguration configuration;
    private String propertyName;
    private Function<PipelineRenderingContext, Object> value;

    public ValuePipelineNode(PipelineNodeConfiguration configuration, String propertyName, final Object value) {
        this.configuration = configuration;
        this.propertyName = propertyName;
        this.value = new Function<PipelineRenderingContext, Object>() {
            @Override
            public Object apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                return value;
            }
        };
    }

    @Override
    public PropertyType determinePropertyType(String name, List<PropertyType> acceptedPropertyTypes) {
        List<PropertyType> producablePropertyTypes = configuration.getNodeOutput(name).getProducablePropertyTypes();
        for (PropertyType acceptedPropertyType : acceptedPropertyTypes) {
            if (producablePropertyTypes.contains(acceptedPropertyType))
                return acceptedPropertyType;
        }
        return null;
    }

    @Override
    public void startFrame(float delta) {

    }

    @Override
    public Function<PipelineRenderingContext, ?> getOutputSupplier(String name, PropertyType propertyType) {
        if (!name.equals(propertyName))
            throw new IllegalArgumentException();
        return value;
    }

    @Override
    public void endFrame() {

    }

    @Override
    public void dispose() {

    }
}
