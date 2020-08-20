package com.gempukku.libgdx.graph.pipeline.loader.postprocessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.BloomPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.FloatFieldOutput;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import org.json.simple.JSONObject;

import java.util.Map;

public class BloomPipelineNodeProducer extends PipelineNodeProducerImpl {
    public BloomPipelineNodeProducer() {
        super(new BloomPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        final ShaderProgram brightnessFilterPassProgram = new ShaderProgram(
                Gdx.files.internal("shader/viewToScreenCoords.vert"),
                Gdx.files.internal("shader/brightnessFilter.frag"));
        if (!brightnessFilterPassProgram.isCompiled())
            throw new IllegalArgumentException("Error compiling shader: " + brightnessFilterPassProgram.getLog());
        final ShaderProgram gaussianBlurPassProgram = new ShaderProgram(
                Gdx.files.internal("shader/viewToScreenCoords.vert"),
                Gdx.files.internal("shader/gaussianBlur.frag"));
        if (!gaussianBlurPassProgram.isCompiled())
            throw new IllegalArgumentException("Error compiling shader: " + gaussianBlurPassProgram.getLog());
        final ShaderProgram bloomSumProgram = new ShaderProgram(
                Gdx.files.internal("shader/viewToScreenCoords.vert"),
                Gdx.files.internal("shader/bloomSum.frag"));
        if (!bloomSumProgram.isCompiled())
            throw new IllegalArgumentException("Error compiling shader: " + bloomSumProgram.getLog());

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

        PipelineNode.FieldOutput<Float> bloomRadius = (PipelineNode.FieldOutput<Float>) inputFields.get("bloomRadius");
        PipelineNode.FieldOutput<Float> minimalBrightness = (PipelineNode.FieldOutput<Float>) inputFields.get("minimalBrightness");
        PipelineNode.FieldOutput<Float> bloomStrength = (PipelineNode.FieldOutput<Float>) inputFields.get("bloomStrength");
        if (bloomRadius == null)
            bloomRadius = new FloatFieldOutput(1f);
        if (minimalBrightness == null)
            minimalBrightness = new FloatFieldOutput(0.7f);
        if (bloomStrength == null)
            bloomStrength = new FloatFieldOutput(0f);
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        final PipelineNode.FieldOutput<Float> finalBloomStrength = bloomStrength;
        final PipelineNode.FieldOutput<Float> finalBloomRadius = bloomRadius;
        final PipelineNode.FieldOutput<Float> finalMinimalBrightness = minimalBrightness;
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext);

                float bloomStrengthValue = finalBloomStrength.getValue(pipelineRenderingContext);
                int bloomRadiusValue = MathUtils.round(finalBloomRadius.getValue(pipelineRenderingContext));
                if (bloomStrengthValue > 0 && bloomRadiusValue > 0) {
                    float minimalBrightnessValue = finalMinimalBrightness.getValue(pipelineRenderingContext);

                    FrameBuffer originalBuffer = renderPipeline.getCurrentBuffer();

                    FrameBuffer brightnessFilterBuffer = runBrightnessPass(minimalBrightnessValue, renderPipeline, originalBuffer, brightnessFilterPassProgram, vertexBufferObject, indexBufferObject);

                    FrameBuffer gaussianBlur = applyGaussianBlur(bloomRadiusValue, renderPipeline,
                            brightnessFilterBuffer, gaussianBlurPassProgram, vertexBufferObject, indexBufferObject);
                    renderPipeline.returnFrameBuffer(brightnessFilterBuffer);

                    FrameBuffer result = applyTheBloom(bloomStrengthValue, renderPipeline, originalBuffer, gaussianBlur, bloomSumProgram, vertexBufferObject, indexBufferObject);

                    renderPipeline.returnFrameBuffer(gaussianBlur);
                    renderPipeline.returnFrameBuffer(originalBuffer);
                    renderPipeline.setCurrentBuffer(result);
                }

                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            @Override
            public void dispose() {
                vertexBufferObject.dispose();
                indexBufferObject.dispose();
                bloomSumProgram.dispose();
                gaussianBlurPassProgram.dispose();
                brightnessFilterPassProgram.dispose();
            }
        };
    }

    private FrameBuffer applyTheBloom(float bloomStrength, RenderPipeline renderPipeline, FrameBuffer sourceBuffer, FrameBuffer gaussianBlur,
                                      ShaderProgram bloomSumProgram, VertexBufferObject vertexBufferObject, IndexBufferObject indexBufferObject) {
        FrameBuffer result = renderPipeline.getNewFrameBuffer(sourceBuffer);

        result.begin();

        bloomSumProgram.bind();

        vertexBufferObject.bind(bloomSumProgram);
        indexBufferObject.bind();

        gaussianBlur.getColorBufferTexture().bind(1);
        bloomSumProgram.setUniformi("u_brightnessTexture", 1);

        sourceBuffer.getColorBufferTexture().bind(0);
        bloomSumProgram.setUniformi("u_sourceTexture", 0);

        bloomSumProgram.setUniformf("u_bloomStrength", bloomStrength);

        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexBufferObject.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);
        vertexBufferObject.unbind(bloomSumProgram);
        indexBufferObject.unbind();

        result.end();

        return result;
    }


    private FrameBuffer applyGaussianBlur(int bloomRadius, RenderPipeline renderPipeline, FrameBuffer sourceBuffer,
                                          ShaderProgram blurProgram, VertexBufferObject vertexBufferObject, IndexBufferObject indexBufferObject) {
        float[] kernel = GaussianBlurKernel.getKernel(MathUtils.round(bloomRadius));
        blurProgram.bind();
        blurProgram.setUniformi("u_sourceTexture", 0);
        blurProgram.setUniformi("u_blurRadius", bloomRadius);
        blurProgram.setUniformf("u_pixelSize", 1f / sourceBuffer.getWidth(), 1f / sourceBuffer.getHeight());
        blurProgram.setUniform1fv("u_kernel", kernel, 0, kernel.length);

        vertexBufferObject.bind(blurProgram);
        indexBufferObject.bind();

        blurProgram.setUniformi("u_vertical", 1);
        FrameBuffer tempBuffer = executeBlur(renderPipeline, sourceBuffer, indexBufferObject);
        blurProgram.setUniformi("u_vertical", 0);
        FrameBuffer blurredBuffer = executeBlur(renderPipeline, tempBuffer, indexBufferObject);
        renderPipeline.returnFrameBuffer(tempBuffer);

        indexBufferObject.unbind();
        vertexBufferObject.unbind(blurProgram);

        return blurredBuffer;
    }

    private FrameBuffer executeBlur(RenderPipeline renderPipeline, FrameBuffer sourceBuffer, IndexBufferObject indexBufferObject) {
        FrameBuffer resultBuffer = renderPipeline.getNewFrameBuffer(sourceBuffer);
        resultBuffer.begin();

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, sourceBuffer.getColorBufferTexture().getTextureObjectHandle());

        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexBufferObject.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);

        resultBuffer.end();

        return resultBuffer;
    }

    private FrameBuffer runBrightnessPass(float minimalBrightnessValue, RenderPipeline renderPipeline, FrameBuffer currentBuffer, ShaderProgram brightnessFilterPassProgram, VertexBufferObject vertexBufferObject, IndexBufferObject indexBufferObject) {
        FrameBuffer brightnessFilterBuffer = renderPipeline.getNewFrameBuffer(currentBuffer);

        brightnessFilterBuffer.begin();

        brightnessFilterPassProgram.bind();

        vertexBufferObject.bind(brightnessFilterPassProgram);
        indexBufferObject.bind();

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0 + 0);
        Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, currentBuffer.getColorBufferTexture().getTextureObjectHandle());

        brightnessFilterPassProgram.setUniformi("u_sourceTexture", 0);
        brightnessFilterPassProgram.setUniformf("u_minimalBrightness", minimalBrightnessValue);

        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexBufferObject.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);
        vertexBufferObject.unbind(brightnessFilterPassProgram);
        indexBufferObject.unbind();

        brightnessFilterBuffer.end();

        return brightnessFilterBuffer;
    }
}
