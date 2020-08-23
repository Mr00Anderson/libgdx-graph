package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RenderPipelineImpl implements RenderPipeline {
    private BufferCopyHelper bufferCopyHelper = new BufferCopyHelper();
    private FrameBuffer mainBuffer;

    private List<FrameBuffer> oldFrameBuffers = new LinkedList<FrameBuffer>();
    private List<FrameBuffer> newFrameBuffers = new LinkedList<FrameBuffer>();

    public void ageOutBuffers() {
        for (FrameBuffer freeFrameBuffer : oldFrameBuffers) {
            freeFrameBuffer.dispose();
        }
        oldFrameBuffers.clear();
        oldFrameBuffers.addAll(newFrameBuffers);
        newFrameBuffers.clear();
    }

    public void cleanup() {
        for (FrameBuffer freeFrameBuffer : oldFrameBuffers) {
            freeFrameBuffer.dispose();
        }
        for (FrameBuffer freeFrameBuffer : newFrameBuffers) {
            freeFrameBuffer.dispose();
        }
        oldFrameBuffers.clear();
        newFrameBuffers.clear();
    }

    @Override
    public void setCurrentBuffer(FrameBuffer frameBuffer) {
        mainBuffer = frameBuffer;
    }

    @Override
    public FrameBuffer getNewFrameBuffer(FrameBuffer takeSettingsFrom) {
        return getNewFrameBuffer(takeSettingsFrom.getWidth(), takeSettingsFrom.getHeight(),
                takeSettingsFrom.getColorBufferTexture().getTextureData().getFormat());
    }

    @Override
    public FrameBuffer getNewFrameBuffer(int width, int height, Pixmap.Format format) {
        FrameBuffer buffer = extractFrameBuffer(width, height, this.newFrameBuffers);
        if (buffer != null) return buffer;
        buffer = extractFrameBuffer(width, height, this.oldFrameBuffers);
        if (buffer != null) return buffer;

        GLFrameBuffer.FrameBufferBuilder frameBufferBuilder = new GLFrameBuffer.FrameBufferBuilder(width, height);
        frameBufferBuilder.addBasicColorTextureAttachment(format);
        frameBufferBuilder.addBasicDepthRenderBuffer();
        return frameBufferBuilder.build();
    }

    @Override
    public BufferCopyHelper getBufferCopyHelper() {
        return bufferCopyHelper;
    }

    private FrameBuffer extractFrameBuffer(int width, int height, List<FrameBuffer> frameBuffers) {
        Iterator<FrameBuffer> iterator = frameBuffers.iterator();
        while (iterator.hasNext()) {
            FrameBuffer buffer = iterator.next();
            if (buffer.getWidth() == width && buffer.getHeight() == height) {
                iterator.remove();
                return buffer;
            }
        }
        return null;
    }

    @Override
    public void returnFrameBuffer(FrameBuffer frameBuffer) {
        newFrameBuffers.add(frameBuffer);
    }

    public FrameBuffer getCurrentBuffer() {
        return mainBuffer;
    }

    public void dispose() {
        cleanup();
    }
}
