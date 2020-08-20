package com.gempukku.libgdx.graph.pipeline.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

public class BufferCopyHelper {
    private SpriteBatch batch;

    public BufferCopyHelper() {
        batch = new SpriteBatch();
    }

    public void copy(FrameBuffer from, FrameBuffer to) {
        copy(from, to, 0, 0, getBufferWidth(to), getBufferHeight(to));
    }

    public void copy(FrameBuffer from, FrameBuffer to, float x, float y, float width, float height) {
        if (to != null) {
            to.begin();
        }

        // Need to flip vertically, because of coordinate differences in Sprites and 3D
        int bufferWidth = getBufferWidth(to);
        int bufferHeight = getBufferHeight(to);

        batch.getProjectionMatrix().setToOrtho2D(0, 0, bufferWidth, bufferHeight);
        batch.begin();
        batch.draw(from.getColorBufferTexture(), x, y + height, width, -height);
        batch.end();

        if (to != null) {
            to.end();
        }
    }

    public void dispose() {
        batch.dispose();
    }

    private int getBufferWidth(FrameBuffer to) {
        if (to == null)
            return Gdx.graphics.getWidth();
        return to.getWidth();
    }

    private int getBufferHeight(FrameBuffer to) {
        if (to == null)
            return Gdx.graphics.getHeight();
        return to.getHeight();
    }
}
