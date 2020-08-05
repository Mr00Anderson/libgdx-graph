package com.gempukku.libgdx.graph.renderer;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.gempukku.libgdx.graph.renderer.impl.BufferCopyHelper;

public interface RenderPipeline {
    FrameBuffer getCurrentBuffer();

    void setCurrentBuffer(FrameBuffer frameBuffer);

    FrameBuffer getNewFrameBuffer(int width, int height, Pixmap.Format format);

    FrameBuffer getNewFrameBuffer(FrameBuffer takeSettingsFrom);

    void returnFrameBuffer(FrameBuffer frameBuffer);

    BufferCopyHelper getBufferCopyHelper();
}
