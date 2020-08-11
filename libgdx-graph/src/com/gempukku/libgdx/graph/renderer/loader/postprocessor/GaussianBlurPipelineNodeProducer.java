package com.gempukku.libgdx.graph.renderer.loader.postprocessor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.IndexBufferObject;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.VertexBufferObject;
import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.config.postprocessor.GaussianBlurPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.Map;

public class GaussianBlurPipelineNodeProducer extends PipelineNodeProducerImpl {
    public GaussianBlurPipelineNodeProducer() {
        super(new GaussianBlurPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        final ShaderProgram shaderProgram = new ShaderProgram(
                Gdx.files.internal("shader/viewToScreenCoords.vert"),
                Gdx.files.internal("shader/gaussianBlur.frag"));
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

        Function<PipelineRenderingContext, Float> blurRadius = (Function<PipelineRenderingContext, Float>) inputFunctions.get("blurRadius");
        if (blurRadius == null)
            blurRadius = new Function<PipelineRenderingContext, Float>() {
                @Override
                public Float apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                    return 0f;
                }
            };
        final Function<PipelineRenderingContext, RenderPipeline> renderPipelineInput = (Function<PipelineRenderingContext, RenderPipeline>) inputFunctions.get("input");

        final Function<PipelineRenderingContext, Float> finalBlurRadius = blurRadius;
        return new OncePerFrameJobPipelineNode(getConfiguration()) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.apply(pipelineRenderingContext);

                int blurRadius = MathUtils.round(finalBlurRadius.apply(pipelineRenderingContext));
                if (blurRadius > 0) {
                    float[] kernel = GaussianBlurKernel.getKernel(blurRadius);
                    FrameBuffer currentBuffer = renderPipeline.getCurrentBuffer();

                    shaderProgram.bind();
                    shaderProgram.setUniformi("u_sourceTexture", 0);
                    shaderProgram.setUniformi("u_blurRadius", blurRadius);
                    shaderProgram.setUniformf("u_pixelSize", 1f / currentBuffer.getWidth(), 1f / currentBuffer.getHeight());
                    shaderProgram.setUniform1fv("u_kernel", kernel, 0, kernel.length);

                    vertexBufferObject.bind(shaderProgram);
                    indexBufferObject.bind();

                    shaderProgram.setUniformi("u_vertical", 1);
                    FrameBuffer tempBuffer = executeBlur(renderPipeline, currentBuffer, indexBufferObject);
                    renderPipeline.returnFrameBuffer(currentBuffer);
                    shaderProgram.setUniformi("u_vertical", 0);
                    FrameBuffer finalBuffer = executeBlur(renderPipeline, tempBuffer, indexBufferObject);
                    renderPipeline.returnFrameBuffer(tempBuffer);
                    renderPipeline.setCurrentBuffer(finalBuffer);

                    indexBufferObject.unbind();
                    vertexBufferObject.unbind(shaderProgram);
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

    private FrameBuffer executeBlur(RenderPipeline renderPipeline, FrameBuffer sourceBuffer, IndexBufferObject indexBufferObject) {

        FrameBuffer resultBuffer = renderPipeline.getNewFrameBuffer(sourceBuffer);
        resultBuffer.begin();

        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
        Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, sourceBuffer.getColorBufferTexture().getTextureObjectHandle());

        Gdx.gl20.glDrawElements(Gdx.gl20.GL_TRIANGLES, indexBufferObject.getNumIndices(), GL20.GL_UNSIGNED_SHORT, 0);

        resultBuffer.end();

        return resultBuffer;
    }
}
