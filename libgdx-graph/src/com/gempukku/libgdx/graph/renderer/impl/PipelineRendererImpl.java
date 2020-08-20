package com.gempukku.libgdx.graph.renderer.impl;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.PipelineProperty;
import com.gempukku.libgdx.graph.renderer.PipelinePropertySource;
import com.gempukku.libgdx.graph.renderer.PipelineRenderer;
import com.gempukku.libgdx.graph.renderer.RenderOutput;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.rendering.node.EndPipelineNode;

import java.util.Map;

public class PipelineRendererImpl implements PipelineRenderer {
    private Iterable<PipelineNode> nodes;
    private Map<String, WritablePipelineProperty<PipelineFieldType>> pipelinePropertyMap;
    private EndPipelineNode endNode;

    public PipelineRendererImpl(Iterable<PipelineNode> nodes, Map<String, WritablePipelineProperty<PipelineFieldType>> pipelinePropertyMap, EndPipelineNode endNode) {
        this.nodes = nodes;
        this.pipelinePropertyMap = pipelinePropertyMap;
        this.endNode = endNode;
    }

    @Override
    public void setPipelineProperty(String property, Object value) {
        WritablePipelineProperty<PipelineFieldType> propertyValue = pipelinePropertyMap.get(property);
        if (propertyValue == null)
            throw new IllegalArgumentException("Property with name not found: " + property);
        FieldType fieldType = propertyValue.getPropertyType();
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
        WritablePipelineProperty<PipelineFieldType> propertyValue = pipelinePropertyMap.get(property);
        if (propertyValue == null)
            throw new IllegalArgumentException("Property with name not found: " + property);
        propertyValue.unsetValue();
    }

    @Override
    public PipelineProperty<PipelineFieldType> getPipelineProperty(String property) {
        return pipelinePropertyMap.get(property);
    }

    @Override
    public Iterable<? extends PipelineProperty<PipelineFieldType>> getProperties() {
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
