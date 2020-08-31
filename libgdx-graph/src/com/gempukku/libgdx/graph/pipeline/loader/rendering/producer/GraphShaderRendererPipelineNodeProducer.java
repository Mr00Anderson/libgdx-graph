package com.gempukku.libgdx.graph.pipeline.loader.rendering.producer;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
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
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderAttribute;
import com.gempukku.libgdx.graph.shader.ShaderLoaderCallback;
import org.json.simple.JSONObject;

import java.util.Comparator;
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

        final PipelineNode.FieldOutput<PipelineRendererModels> modelsInput = (PipelineNode.FieldOutput<PipelineRendererModels>) inputFields.get("models");
        final PipelineNode.FieldOutput<Environment> lightsInput = (PipelineNode.FieldOutput<Environment>) inputFields.get("lights");
        final PipelineNode.FieldOutput<Camera> cameraInput = (PipelineNode.FieldOutput<Camera>) inputFields.get("camera");
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            private final RenderContext renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.LRU, 1));
            private final Array<Renderable> renderables = new Array<Renderable>();
            private final RenderablePool renderablePool = new RenderablePool();
            private DistanceRenderableSorter distanceRenderableSorter = new DistanceRenderableSorter();

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
                distanceRenderableSorter.sort(camera, renderables);

                // First render opaque models
                for (Map.Entry<String, GraphShader> shaderEntry : shaders.entrySet()) {
                    String tag = shaderEntry.getKey();
                    GraphShader shader = shaderEntry.getValue();
                    if (shader.getTransparency() == BasicShader.Transparency.opaque) {
                        shader.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                        shader.setEnvironment(environment);
                        renderWithShaderReverseOrder(tag, shader, camera, environment);
                    }
                }
                // Then render transparent models
                GraphShader lastShader = null;
                for (Renderable renderable : renderables) {
                    for (Map.Entry<String, GraphShader> shaderEntry : shaders.entrySet()) {
                        String tag = shaderEntry.getKey();
                        GraphShader shader = shaderEntry.getValue();
                        if (shader.getTransparency() == BasicShader.Transparency.transparent) {
                            shader.setTimeProvider(pipelineRenderingContext.getTimeProvider());
                            shader.setEnvironment(environment);
                            GraphShaderAttribute graphShaderAttribute = renderable.material.get(GraphShaderAttribute.class, GraphShaderAttribute.GraphShader);
                            if (graphShaderAttribute.hasTag(tag)) {
                                if (lastShader != shader) {
                                    if (lastShader != null)
                                        lastShader.end();
                                    shader.begin(camera, environment, renderContext);
                                }
                                shader.render(renderable);
                                lastShader = shader;
                            }
                        }
                    }
                }
                if (lastShader != null)
                    lastShader.end();
                renderables.clear();

                currentBuffer.end();
                renderContext.end();
                OutputValue<RenderPipeline> output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }

            private void renderWithShaderReverseOrder(String tag, GraphShader shader, Camera camera, Environment environment) {
                shader.begin(camera, environment, renderContext);
                for (int i = renderables.size - 1; i >= 0; i--) {
                    Renderable renderable = renderables.get(i);
                    GraphShaderAttribute graphShaderAttribute = renderable.material.get(GraphShaderAttribute.class, GraphShaderAttribute.GraphShader);
                    if (graphShaderAttribute.hasTag(tag))
                        shader.render(renderable);
                }
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

    private static class DistanceRenderableSorter implements RenderableSorter, Comparator<Renderable> {
        private Camera camera;
        private final Vector3 tmpV1 = new Vector3();
        private final Vector3 tmpV2 = new Vector3();

        @Override
        public void sort(Camera camera, Array<Renderable> renderables) {
            this.camera = camera;
            renderables.sort(this);
        }

        private Vector3 getTranslation(Matrix4 worldTransform, Vector3 center, Vector3 output) {
            if (center.isZero())
                worldTransform.getTranslation(output);
            else if (!worldTransform.hasRotationOrScaling())
                worldTransform.getTranslation(output).add(center);
            else
                output.set(center).mul(worldTransform);
            return output;
        }

        @Override
        public int compare(Renderable o1, Renderable o2) {
            getTranslation(o1.worldTransform, o1.meshPart.center, tmpV1);
            getTranslation(o2.worldTransform, o2.meshPart.center, tmpV2);
            final float dst = (int) (1000f * camera.position.dst2(tmpV1)) - (int) (1000f * camera.position.dst2(tmpV2));
            return dst < 0 ? 1 : (dst > 0 ? -1 : 0);
        }
    }
}
