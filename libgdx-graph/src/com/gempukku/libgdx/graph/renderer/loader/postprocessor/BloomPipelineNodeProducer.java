package com.gempukku.libgdx.graph.renderer.loader.postprocessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.config.postprocessor.BloomPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.Map;

public class BloomPipelineNodeProducer extends PipelineNodeProducerImpl {
    public BloomPipelineNodeProducer() {
        super(new BloomPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        final ShaderProgram shaderProgram = new ShaderProgram(
                Gdx.files.internal("shader/viewToScreenCoords.vert"),
                Gdx.files.internal("shader/bloom.frag"));
        if (!shaderProgram.isCompiled())
            throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());

        float[] verticeData = new float[]{
                0, 0, 0,
                0, 1, 0,
                1, 0, 0,
                1, 1, 0};
        short[] indices = {0, 1, 2, 2, 1, 3};

        final VertexBufferObject vertexBufferObject = new VertexBufferObject(true, 4, VertexAttribute.Position());
        final IndexBufferObject indexBufferObject = new IndexBufferObject(true, indices.length);
        vertexBufferObject.setVertices(verticeData, 0, verticeData.length);
        indexBufferObject.setIndices(indices, 0, indices.length);

        Function<PipelineRenderingContext, Float> bloomRadius = (Function<PipelineRenderingContext, Float>) inputFunctions.get("bloomRadius");
        Function<PipelineRenderingContext, Float> minimalBrightness = (Function<PipelineRenderingContext, Float>) inputFunctions.get("minimalBrightness");
        Function<PipelineRenderingContext, Float> bloomStrength = (Function<PipelineRenderingContext, Float>) inputFunctions.get("bloomStrength");
        if (bloomRadius == null)
            bloomRadius = new Function<PipelineRenderingContext, Float>() {
                @Override
                public Float apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                    return 1f;
                }
            };
        if (minimalBrightness == null)
            minimalBrightness = new Function<PipelineRenderingContext, Float>() {
                @Override
                public Float apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                    return 0.7f;
                }
            };
        if (bloomStrength == null)
            bloomStrength = new Function<PipelineRenderingContext, Float>() {
                @Override
                public Float apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                    return 0f;
                }
            };
        final Function<PipelineRenderingContext, RenderPipeline> renderPipelineInput = (Function<PipelineRenderingContext, RenderPipeline>) inputFunctions.get("input");

        final Function<PipelineRenderingContext, Float> finalBloomStrength = bloomStrength;
        final Function<PipelineRenderingContext, Float> finalBloomRadius = bloomRadius;
        final Function<PipelineRenderingContext, Float> finalMinimalBrightness = minimalBrightness;
        return new OncePerFrameJobPipelineNode(getConfiguration()) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.apply(pipelineRenderingContext);

                float bloomStrengthValue = finalBloomStrength.apply(pipelineRenderingContext);
                if (bloomStrengthValue > 0) {
                    float bloomRadiusValue = finalBloomRadius.apply(pipelineRenderingContext);
                    float minimalBrightnessValue = finalMinimalBrightness.apply(pipelineRenderingContext);

                    FrameBuffer currentBuffer = renderPipeline.getCurrentBuffer();

                    int width = currentBuffer.getWidth();
                    int height = currentBuffer.getHeight();

                    FrameBuffer newBuffer = renderPipeline.getNewFrameBuffer(currentBuffer);

                    newBuffer.begin();

                    shaderProgram.begin();

                    vertexBufferObject.bind(shaderProgram);
                    indexBufferObject.bind();

                    Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0 + 0);
                    Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, currentBuffer.getColorBufferTexture().getTextureObjectHandle());

                    shaderProgram.setUniformf("u_sourceTexture", 0);
                    shaderProgram.setUniformf("u_minimalBrightness", minimalBrightnessValue);
                    shaderProgram.setUniformf("u_pixelSize", 1f / width, 1f / height);
                    shaderProgram.setUniformf("u_bloomRadius", bloomRadiusValue);
                    shaderProgram.setUniformf("u_bloomStrength", bloomStrengthValue);

                    Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexBufferObject.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);
                    vertexBufferObject.unbind(shaderProgram);
                    indexBufferObject.unbind();

                    shaderProgram.end();

                    newBuffer.end();

                    renderPipeline.returnFrameBuffer(currentBuffer);
                    renderPipeline.setCurrentBuffer(newBuffer);
                }

                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            @Override
            public void dispose() {
                vertexBufferObject.dispose();
                indexBufferObject.dispose();
                shaderProgram.dispose();
            }
        };
    }
}
