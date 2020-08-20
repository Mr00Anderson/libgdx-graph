package com.gempukku.libgdx.graph.graph.pipeline.impl;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineProperty;
import com.gempukku.libgdx.graph.graph.pipeline.PipelinePropertySource;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.graph.pipeline.RenderOutput;
import com.gempukku.libgdx.graph.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.graph.pipeline.loader.rendering.node.EndPipelineNode;

import java.util.Map;

public class PipelineRendererImpl implements PipelineRenderer {
    private Iterable<PipelineNode> nodes;
    private Map<String, WritablePipelineProperty> pipelinePropertyMap;
    private EndPipelineNode endNode;

    public PipelineRendererImpl(Iterable<PipelineNode> nodes, Map<String, WritablePipelineProperty> pipelinePropertyMap, EndPipelineNode endNode) {
        this.nodes = nodes;
        this.pipelinePropertyMap = pipelinePropertyMap;
        this.endNode = endNode;
    }

    @Override
    public void setPipelineProperty(String property, Object value) {
        WritablePipelineProperty propertyValue = pipelinePropertyMap.get(property);
        if (propertyValue == null)
            throw new IllegalArgumentException("Property with name not found: " + property);
        FieldType fieldType = propertyValue.getType();
        if (!fieldType.accepts(value))
            throw new IllegalArgumentException("Property value not accepted, property: " + property);
        propertyValue.setValue(fieldType.convert(value));
    }

    @Override
    public boolean hasPipelineProperty(String property) {
        return pipelinePropertyMap.containsKey(property);
    }

    @Override
    public void unsetPipelineProperty(String property) {
        WritablePipelineProperty propertyValue = pipelinePropertyMap.get(property);
        if (propertyValue == null)
            throw new IllegalArgumentException("Property with name not found: " + property);
        propertyValue.unsetValue();
    }

    @Override
    public PipelineProperty getPipelineProperty(String property) {
        return pipelinePropertyMap.get(property);
    }

    @Override
    public Iterable<? extends PipelineProperty> getProperties() {
        return pipelinePropertyMap.values();
    }

    @Override
    public void render(float delta, final RenderOutput renderOutput) {
        for (PipelineNode node : nodes) {
            node.startFrame(delta);
        }

        PipelineRenderingContext context = new PipelineRenderingContext() {
            @Override
            public int getRenderWidth() {
                return renderOutput.getRenderWidth();
            }

            @Override
            public int getRenderHeight() {
                return renderOutput.getRenderHeight();
            }

            @Override
            public PipelinePropertySource getPipelinePropertySource() {
                return PipelineRendererImpl.this;
            }
        };

        RenderPipeline renderPipeline = endNode.executePipeline(context);
        renderOutput.output(renderPipeline);

        for (PipelineNode node : nodes) {
            node.endFrame();
        }
    }

    @Override
    public void dispose() {
        for (PipelineNode node : nodes) {
            node.dispose();
        }
    }
}
