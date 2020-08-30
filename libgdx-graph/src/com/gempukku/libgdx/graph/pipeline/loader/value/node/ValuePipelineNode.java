package com.gempukku.libgdx.graph.pipeline.loader.value.node;

import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;

public class ValuePipelineNode implements PipelineNode {
    private NodeConfiguration<PipelineFieldType> configuration;
    private String propertyName;
    private Object value;

    public ValuePipelineNode(NodeConfiguration<PipelineFieldType> configuration, String propertyName, final Object value) {
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
                return configuration.getNodeOutputs().get(name).determineFieldType(null);
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
