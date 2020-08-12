package com.gempukku.libgdx.graph.renderer;

import com.badlogic.gdx.Gdx;
import com.gempukku.libgdx.graph.renderer.impl.BufferCopyHelper;

public class RenderOutputs {
    public static final RenderOutput drawToScreen = new DrawToScreen();

    private static class DrawToScreen implements RenderOutput {
        @Override
        public int getRenderWidth() {
            return Gdx.graphics.getWidth();
        }

        @Override
        public int getRenderHeight() {
            return Gdx.graphics.getHeight();
        }

        @Override
        public void output(RenderPipeline renderPipeline) {
            BufferCopyHelper bufferCopyHelper = renderPipeline.getBufferCopyHelper();
            bufferCopyHelper.copy(renderPipeline.getCurrentBuffer(), null);

            renderPipeline.returnFrameBuffer(renderPipeline.getCurrentBuffer());
        }
    }
}
