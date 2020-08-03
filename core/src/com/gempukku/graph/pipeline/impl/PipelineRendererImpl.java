package com.gempukku.graph.pipeline.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.gempukku.graph.pipeline.LightsProvider;
import com.gempukku.graph.pipeline.ObjectContainer;
import com.gempukku.graph.pipeline.PipelineParticipant;
import com.gempukku.graph.pipeline.PipelineProperty;
import com.gempukku.graph.pipeline.PipelineRenderer;
import com.gempukku.graph.pipeline.RenderPipelineImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PipelineRendererImpl implements PipelineRenderer {
    private RenderPipelineImpl renderPipeline = new RenderPipelineImpl();
    private BufferCopyHelper bufferCopyHelper = new BufferCopyHelper();

    private Map<String, PipelinePropertyImpl> pipelinePropertyMap = new HashMap<>();
    private List<PipelineParticipant> pipelineParticipants = new LinkedList<>();

    private int width;
    private int height;

    @Override
    public void setPipelineProperty(String property, Object value) {
        PipelinePropertyImpl propertyValue = pipelinePropertyMap.get(property);
        if (propertyValue == null)
            throw new IllegalArgumentException("Property with name not found: " + property);
        if (!propertyValue.getPropertyType().accepts(value))
            throw new IllegalArgumentException("Property value not accepted, property: " + property);
        propertyValue.setValue(value);
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
    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(LightsProvider lightsProvider, ObjectContainer objectContainer) {
        try {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            FrameBuffer frameBuffer = renderPipeline.getNewFrameBuffer(width, height, Pixmap.Format.RGBA8888);
            renderPipeline.setCurrentBuffer(frameBuffer);
            for (PipelineParticipant pipelineParticipant : pipelineParticipants) {
                pipelineParticipant.renderToPipeline(renderPipeline, this, lightsProvider, objectContainer);
            }

            bufferCopyHelper.copy(renderPipeline.getCurrentBuffer(), null);

            renderPipeline.returnFrameBuffer(renderPipeline.getCurrentBuffer());
        } finally {
            renderPipeline.ageOutBuffers();
        }
    }

    @Override
    public void dispose() {
        bufferCopyHelper.dispose();
        renderPipeline.dispose();
    }
}
