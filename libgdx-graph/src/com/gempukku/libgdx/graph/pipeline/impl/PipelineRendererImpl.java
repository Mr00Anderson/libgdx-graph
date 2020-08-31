package com.gempukku.libgdx.graph.pipeline.impl;

import com.gempukku.libgdx.graph.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.TimeKeeper;
import com.gempukku.libgdx.graph.TimeProvider;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.pipeline.PipelineProperty;
import com.gempukku.libgdx.graph.pipeline.PipelinePropertySource;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutput;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.node.EndPipelineNode;

import java.util.Map;

public class PipelineRendererImpl implements PipelineRenderer {
    private Iterable<PipelineNode> nodes;
    private Map<String, WritablePipelineProperty> pipelinePropertyMap;
    private EndPipelineNode endNode;
    private TimeKeeper timeKeeper;

    public PipelineRendererImpl(Iterable<PipelineNode> nodes, Map<String, WritablePipelineProperty> pipelinePropertyMap, EndPipelineNode endNode) {
        this.nodes = nodes;
        this.pipelinePropertyMap = pipelinePropertyMap;
        this.endNode = endNode;
        this.timeKeeper = new DefaultTimeKeeper();
    }

    @Override
    public void setTimeKeeper(TimeKeeper timeKeeper) {
        this.timeKeeper = timeKeeper;
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
        timeKeeper.updateTime(delta);

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

            @Override
            public TimeProvider getTimeProvider() {
                return timeKeeper;
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
