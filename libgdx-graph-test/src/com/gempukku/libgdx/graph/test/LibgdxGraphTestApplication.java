package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.gempukku.libgdx.graph.GraphLoader;
import com.gempukku.libgdx.graph.libgdx.LibGDXModels;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.pipeline.RendererLoaderCallback;
import com.gempukku.libgdx.graph.shader.GraphShaderAttribute;

import java.io.IOException;
import java.io.InputStream;

public class LibgdxGraphTestApplication extends ApplicationAdapter {
    private long lastProcessedInput;

    private PipelineRenderer pipelineRenderer;
    private LibGDXModels models;
    private Model sphereModel;
    private Environment environment;
    private Camera camera;

    @Override
    public void create() {
        WhitePixel.initialize();

        ModelBuilder modelBuilder = new ModelBuilder();
        GraphShaderAttribute graphShaderAttribute = new GraphShaderAttribute();
        graphShaderAttribute.addShaderTag("");
        Material material = new Material(TextureAttribute.createDiffuse(WhitePixel.texture), graphShaderAttribute);
        sphereModel = modelBuilder.createSphere(1, 1, 1, 20, 20,
                material,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        models = new LibGDXModels();
        models.addRenderableProvider(new ModelInstance(sphereModel));

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camera = new PerspectiveCamera();
        camera.position.set(5f, 5f, 5f);
        camera.lookAt(0, 0, 0);
        camera.update();

        pipelineRenderer = loadPipelineRenderer();
    }

    private void setPropertyIfDefined(String propertyName, float value) {
        if (pipelineRenderer.hasPipelineProperty(propertyName))
            pipelineRenderer.setPipelineProperty(propertyName, value);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        reloadRendererIfNeeded();

        pipelineRenderer.render(delta, RenderOutputs.drawToScreen);
    }

    private void reloadRendererIfNeeded() {
        long currentTime = System.currentTimeMillis();
        if (lastProcessedInput + 200 < currentTime) {
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                lastProcessedInput = currentTime;
                pipelineRenderer.dispose();
                pipelineRenderer = loadPipelineRenderer();
            }
        }
    }

    @Override
    public void dispose() {
        sphereModel.dispose();
        pipelineRenderer.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        try {
            InputStream stream = Gdx.files.local("test.json").read();
            try {
                PipelineRenderer pipelineRenderer = GraphLoader.loadGraph(stream, new RendererLoaderCallback());
                setupPipeline(pipelineRenderer);
                return pipelineRenderer;
            } finally {
                stream.close();
            }
        } catch (IOException exp) {
            throw new RuntimeException("Unable to load pipeline", exp);
        }
    }

    private void setupPipeline(PipelineRenderer pipelineRenderer) {
        //pipelineRenderer.setPipelineProperty("Background Color", Color.RED);
//        pipelineRenderer.setPipelineProperty("Stage", stage);
        pipelineRenderer.setPipelineProperty("Models", models);
//        pipelineRenderer.setPipelineProperty("Lights", environment);
        pipelineRenderer.setPipelineProperty("Camera", camera);
    }
}