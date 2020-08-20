package com.gempukku.libgdx.graph.renderer.loader.value.node;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;

public class ValuePipelineNode implements PipelineNode {
    private PipelineNodeConfiguration<PipelineFieldType> configuration;
    private String propertyName;
    private Object value;

    public ValuePipelineNode(PipelineNodeConfiguration<PipelineFieldType> configuration, String propertyName, final Object value) {
        this.configuration = configuration;
        this.propertyName = propertyName;
        this.value = value;
    }

    @Override
    public FieldOutput<?> getFieldOutput(final String name) {
        if (!name.equals(propertyName))
            throw new IllegalArgumentException();
        return new FieldOutput<Object>() {
            @Override
            public PipelineFieldType getPropertyType() {
                return configuration.getNodeOutput(name).getProducablePropertyTypes().get(0);
            }

            @Override
            public Object getValue(PipelineRenderingContext context) {
                return value;
            }
        };
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
}
