package com.gempukku.libgdx.graph.pipeline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.graph.pipeline.impl.BufferCopyHelper;

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

    public static class RenderToTexture implements RenderOutput, Disposable {
        private ProvidedTextureFrameBuffer frameBuffer;
        private Texture texture;

        public RenderToTexture(Texture texture) {
            this.texture = texture;
            frameBuffer = new ProvidedTextureFrameBuffer(texture);
        }

        public void setTexture(Texture texture) {
            this.texture = texture;
            frameBuffer.setTexture(texture);
        }

        @Override
        public int getRenderWidth() {
            return texture.getWidth();
        }

        @Override
        public int getRenderHeight() {
            return texture.getHeight();
        }

        @Override
        public void output(RenderPipeline renderPipeline) {
            FrameBuffer currentBuffer = renderPipeline.getCurrentBuffer();
            renderPipeline.getBufferCopyHelper().copy(currentBuffer, frameBuffer);
            renderPipeline.returnFrameBuffer(currentBuffer);
        }

        @Override
        public void dispose() {
            frameBuffer.dispose();
        }
    }

    private static class ProvidedTextureFrameBuffer extends FrameBuffer {
        public ProvidedTextureFrameBuffer(Texture texture) {
            super(new FrameBufferBuilder(texture.getWidth(), texture.getHeight()));
            setTexture(texture);
        }

        public void setTexture(Texture texture) {
            bind();
            Gdx.gl20.glBindTexture(texture.glTarget, texture.getTextureObjectHandle());
            Gdx.gl20.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_COLOR_ATTACHMENT0, GL20.GL_TEXTURE_2D, texture.getTextureObjectHandle(), 0);
            unbind();
        }
    }
}
