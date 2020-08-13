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
import org.json.simple.JSONObject;

import java.util.Map;

public class DefaultRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    public DefaultRendererPipelineNodeProducer() {
        super(new DefaultRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<PipelineRendererModels> modelsInput = (PipelineNode.FieldOutput<PipelineRendererModels>) inputFields.get("models");
        final PipelineNode.FieldOutput<Environment> lightsInput = (PipelineNode.FieldOutput<Environment>) inputFields.get("lights");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            private ModelBatch modelBatch = new ModelBatch();

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext);
                PipelineRendererModels models = modelsInput.getValue(pipelineRenderingContext);
                Camera camera = cameraInput.getValue(pipelineRenderingContext);
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
                Environment environment = lightsInput != null ? lightsInput.getValue(pipelineRenderingContext) : null;
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
