package com.gempukku.libgdx.graph.renderer.loader.rendering.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.gempukku.libgdx.graph.renderer.PipelineRendererModels;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.config.rendering.DefaultRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import java.util.Map;

public class DefaultRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    public DefaultRendererPipelineNodeProducer() {
        super(new DefaultRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, final Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        final Function<PipelineRenderingContext, PipelineRendererModels> modelsInput = (Function<PipelineRenderingContext, PipelineRendererModels>) inputFunctions.get("models");
        final Function<PipelineRenderingContext, Environment> lightsInput = (Function<PipelineRenderingContext, Environment>) inputFunctions.get("lights");
        final Function<PipelineRenderingContext, Camera> cameraInput = (Function<PipelineRenderingContext, Camera>) inputFunctions.get("camera");
        final Function<PipelineRenderingContext, RenderPipeline> renderPipelineInput = (Function<PipelineRenderingContext, RenderPipeline>) inputFunctions.get("input");

        return new OncePerFrameJobPipelineNode(configuration) {
            private ModelBatch modelBatch = new ModelBatch();

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.apply(pipelineRenderingContext);
                PipelineRendererModels models = modelsInput.apply(pipelineRenderingContext);
                Camera camera = cameraInput.apply(pipelineRenderingContext);
                FrameBuffer currentBuffer = renderPipeline.getCurrentBuffer();
                int width = currentBuffer.getWidth();
                int height = currentBuffer.getHeight();
                float viewportWidth = camera.viewportWidth;
                float viewportHeight = camera.viewportHeight;
                if (width != viewportWidth || height != viewportHeight) {
                    camera.viewportWidth = width;
                    camera.viewportHeight = height;
                    camera.update();
                }
                Environment environment = lightsInput != null ? lightsInput.apply(pipelineRenderingContext) : null;
                if (environment != null) {
                    currentBuffer.begin();
                    modelBatch.begin(camera);
                    modelBatch.render(models.getModels(), environment);
                    modelBatch.end();
                    currentBuffer.end();
                } else {
                    currentBuffer.begin();
                    modelBatch.begin(camera);
                    modelBatch.render(models.getModels());
                    modelBatch.end();
                    currentBuffer.end();
                }
                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            @Override
            public void dispose() {
                modelBatch.dispose();
            }
        };
    }
}
