package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.gempukku.libgdx.graph.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.config.rendering.GraphShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ShaderLoaderCallback;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstanceImpl;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;
import org.json.simple.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GraphShaderRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    public GraphShaderRendererPipelineNodeProducer() {
        super(new GraphShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        final List<JSONObject> shaderDefinitions = (List<JSONObject>) data.get("shaders");
        final Map<String, GraphShader> shaders = new LinkedHashMap<>();
        for (JSONObject shaderDefinition : shaderDefinitions) {
            String tag = (String) shaderDefinition.get("tag");
            shaders.put(tag, createShader(shaderDefinition));
        }

        final PipelineNode.FieldOutput<GraphShaderModels> modelsInput = (PipelineNode.FieldOutput<GraphShaderModels>) inputFields.get("models");
        final PipelineNode.FieldOutput<Environment> lightsInput = (PipelineNode.FieldOutput<Environment>) inputFields.get("lights");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            private final RenderContext renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU, 1));

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.getValue(pipelineRenderingContext);
                GraphShaderModels models = modelsInput.getValue(pipelineRenderingContext);
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
                renderContext.begin();
                Environment environment = lightsInput != null ? lightsInput.getValue(pipelineRenderingContext) : null;
                currentBuffer.begin();

                models.prepareForRendering(camera);


                // Initialize shaders for drawing
                for (GraphShader shader : shaders.values()) {
                    shader.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                    shader.setEnvironment(environment);
                }

                // First render opaque models
                models.orderFrontToBack();
                for (Map.Entry<String, GraphShader> shaderEntry : shaders.entrySet()) {
                    String tag = shaderEntry.getKey();
                    GraphShader shader = shaderEntry.getValue();
                    if (shader.getTransparency() == BasicShader.Transparency.opaque) {
                        renderWithShaderOpaquePass(tag, shader, models, camera, environment);
                    }
                }

                // Then render transparent models
                models.orderBackToFront();
                GraphShader lastShader = null;
                for (GraphShaderModelInstanceImpl graphShaderModelInstance : models.getModels()) {
                    for (Map.Entry<String, GraphShader> shaderEntry : shaders.entrySet()) {
                        String tag = shaderEntry.getKey();
                        GraphShader shader = shaderEntry.getValue();
                        if (shader.getTransparency() == BasicShader.Transparency.transparent) {
                            if (graphShaderModelInstance.hasTag(tag)) {
                                if (lastShader != shader) {
                                    if (lastShader != null)
                                        lastShader.end();
                                    shader.begin(camera, environment, renderContext);
                                }
                                shader.render(graphShaderModelInstance);
                                lastShader = shader;
                            }
                        }
                    }
                }
                if (lastShader != null)
                    lastShader.end();

                currentBuffer.end();
                renderContext.end();
                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            private void renderWithShaderOpaquePass(String tag, GraphShader shader, GraphShaderModels models, Camera camera, Environment environment) {
                boolean begun = false;
                for (GraphShaderModelInstanceImpl graphShaderModelInstance : models.getModelsWithTag(tag)) {
                    if (!begun) {
                        shader.begin(camera, environment, renderContext);
                        begun = true;
                    }
                    shader.render(graphShaderModelInstance);
                }
                if (begun)
                    shader.end();
            }

            @Override
            public void dispose() {
                for (GraphShader shader : shaders.values()) {
                    shader.dispose();
                }
            }
        };
    }

    private GraphShader createShader(JSONObject shaderDefinition) {
        JSONObject shaderGraph = (JSONObject) shaderDefinition.get("shader");
        return GraphLoader.loadGraph(shaderGraph, new ShaderLoaderCallback());
    }
}
