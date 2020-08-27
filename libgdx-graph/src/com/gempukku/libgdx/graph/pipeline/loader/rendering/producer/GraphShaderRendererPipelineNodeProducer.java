package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FlushablePool;
import com.gempukku.libgdx.graph.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererModels;
import com.gempukku.libgdx.graph.pipeline.RenderPipeline;
import com.gempukku.libgdx.graph.pipeline.config.rendering.GraphShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderAttribute;
import com.gempukku.libgdx.graph.shader.ShaderLoaderCallback;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphShaderRendererPipelineNodeProducer extends PipelineNodeProducerImpl {
    public GraphShaderRendererPipelineNodeProducer() {
        super(new GraphShaderRendererPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        List<JSONObject> shaderDefinitions = (List<JSONObject>) data.get("shaders");
        final List<GraphShader> shaders = new LinkedList<>();
        for (JSONObject shaderDefinition : shaderDefinitions) {
            shaders.add(createShader(shaderDefinition));
        }

        final PipelineNode.FieldOutput<PipelineRendererModels> modelsInput = (PipelineNode.FieldOutput<PipelineRendererModels>) inputFields.get("models");
        final PipelineNode.FieldOutput<Environment> lightsInput = (PipelineNode.FieldOutput<Environment>) inputFields.get("lights");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            final RenderContext renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU, 1));
            final Array<Renderable> renderables = new Array<Renderable>();
            final RenderablePool renderablePool = new RenderablePool();

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
                renderContext.begin();
                Environment environment = lightsInput != null ? lightsInput.getValue(pipelineRenderingContext) : null;
                currentBuffer.begin();

                for (RenderableProvider renderableProvider : models.getModels()) {
                    renderableProvider.getRenderables(renderables, renderablePool);
                }
                for (GraphShader shader : shaders) {
                    shader.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                    renderWithShader(shader, camera, models, environment);
                }
                renderables.clear();

                currentBuffer.end();
                renderContext.end();
                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            private void renderWithShader(GraphShader shader, Camera camera, PipelineRendererModels models, Environment environment) {
                String tag = shader.getTag();
                shader.begin(camera, environment, renderContext);
                for (Renderable renderable : renderables) {
                    GraphShaderAttribute graphShaderAttribute = renderable.material.get(GraphShaderAttribute.class, GraphShaderAttribute.GraphShader);
                    if (graphShaderAttribute.hasTag(tag))
                        shader.render(renderable);
                }
                shader.end();
            }

            @Override
            public void dispose() {
                for (GraphShader shader : shaders) {
                    shader.dispose();
                }
            }
        };
    }

    private GraphShader createShader(JSONObject shaderDefinition) {
        String tag = (String) shaderDefinition.get("tag");
        JSONObject shaderGraph = (JSONObject) shaderDefinition.get("shader");
        return GraphLoader.loadGraph(shaderGraph, new ShaderLoaderCallback(tag));
    }

    private static class RenderablePool extends FlushablePool<Renderable> {
        @Override
        protected Renderable newObject() {
            return new Renderable();
        }

        @Override
        public Renderable obtain() {
            Renderable renderable = super.obtain();
            renderable.environment = null;
            renderable.material = null;
            renderable.meshPart.set("", null, 0, 0, 0);
            renderable.shader = null;
            renderable.userData = null;
            return renderable;
        }
    }
}
