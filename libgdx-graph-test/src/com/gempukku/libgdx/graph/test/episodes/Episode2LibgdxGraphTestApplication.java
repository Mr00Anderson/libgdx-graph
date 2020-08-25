package com.gempukku.libgdx.graph.test.episodes;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.GraphLoader;
import com.gempukku.libgdx.graph.libgdx.LibGDXModels;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.test.WhitePixel;

import java.io.IOException;
import java.io.InputStream;

public class Episode2LibgdxGraphTestApplication extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;
    private long lastProcessedInput;

    private PipelineRenderer pipelineRenderer;
    private LibGDXModels models;
    private Model sphereModel;
    private Environment environment;
    private Camera camera;

    @Override
    public void create() {
        WhitePixel.initialize();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        constructStage();

        models = createModels();
        environment = createEnvironment();
        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();

        Gdx.input.setInputProcessor(stage);
    }

    private Camera createCamera() {
        Camera camera = new PerspectiveCamera();
        camera.position.set(5f, 5f, 5f);
        camera.lookAt(0, 0, 0);
        camera.update();
        return camera;
    }

    private Environment createEnvironment() {
        Environment environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        return environment;
    }

    private LibGDXModels createModels() {
        ModelBuilder modelBuilder = new ModelBuilder();
        sphereModel = modelBuilder.createSphere(1, 1, 1, 20, 20,
                new Material(TextureAttribute.createDiffuse(WhitePixel.texture)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        LibGDXModels models = new LibGDXModels();
        models.addRenderableProvider(new ModelInstance(sphereModel));
        return models;
    }

    private void constructStage() {
        stage = new Stage(new ScreenViewport());

        Label label = new Label("This is example label", skin);
        label.setBounds(0, 0, 200, 20);
        TextButton textButton = new TextButton("Test", skin);
        textButton.setBounds(0, 20, 200, 20);

        stage.addActor(label);
        stage.addActor(textButton);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        reloadRendererIfNeeded();
        stage.act();

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
        skin.dispose();
        stage.dispose();
        WhitePixel.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        try {
            InputStream stream = Gdx.files.local("episodes/episode2.json").read();
            try {
                PipelineRenderer pipelineRenderer = GraphLoader.loadGraph(stream, new PipelineLoaderCallback());
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
        if (pipelineRenderer.hasPipelineProperty("Stage"))
            pipelineRenderer.setPipelineProperty("Stage", stage);

        pipelineRenderer.setPipelineProperty("Models", models);
        pipelineRenderer.setPipelineProperty("Lights", environment);
        pipelineRenderer.setPipelineProperty("Camera", camera);
    }
}