package com.gempukku.libgdx.graph.renderer.impl;

import com.gempukku.libgdx.graph.renderer.PipelineProperty;
import com.gempukku.libgdx.graph.renderer.PipelinePropertySource;
import com.gempukku.libgdx.graph.renderer.PipelineRenderer;
import com.gempukku.libgdx.graph.renderer.RenderOutput;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.rendering.node.EndPipelineNode;

import java.util.Map;

public class PipelineRendererImpl implements PipelineRenderer, PipelineRenderingContext {
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
        if (!propertyValue.getPropertyType().accepts(value))
            throw new IllegalArgumentException("Property value not accepted, property: " + property);
        propertyValue.setValue(value);
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
    public PipelinePropertySource getPipelinePropertySource() {
        return this;
    }

    @Override
    public void render(float delta, RenderOutput renderOutput) {
        for (PipelineNode node : nodes) {
            node.startFrame(delta);
        }

        RenderPipeline renderPipeline = endNode.executePipeline(this);
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
