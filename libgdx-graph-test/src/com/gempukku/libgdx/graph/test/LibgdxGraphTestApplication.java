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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.PipelineLoader;
import com.gempukku.libgdx.graph.renderer.PipelineRenderer;
import com.gempukku.libgdx.graph.renderer.PipelineRendererModels;
import com.gempukku.libgdx.graph.renderer.RenderOutputs;
import com.gempukku.libgdx.graph.renderer.RendererLoaderCallback;

import java.io.IOException;
import java.io.InputStream;

public class LibgdxGraphTestApplication extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;
    private long lastProcessedInput;

    private PipelineRenderer pipelineRenderer;
    private PipelineRendererModels models;
    private Model sphereModel;
    private Environment environment;
    private Camera camera;

    @Override
    public void create() {
        WhitePixel.initialize();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        constructStage();

        ModelBuilder modelBuilder = new ModelBuilder();
        sphereModel = modelBuilder.createSphere(1, 1, 1, 20, 20,
                new Material(TextureAttribute.createDiffuse(WhitePixel.texture)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        models = new PipelineRendererModels();
        models.addModelInstance(new ModelInstance(sphereModel));

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.2f, 0.2f, 0.2f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        camera = new PerspectiveCamera();
        camera.position.set(5f, 5f, 5f);
        camera.lookAt(0, 0, 0);
        camera.update();

        pipelineRenderer = loadPipelineRenderer();

        Gdx.input.setInputProcessor(stage);
    }

    private void constructStage() {
        stage = new Stage(new ScreenViewport());

        final Slider bloomRadius = new Slider(0, 50, 1, false, skin);
        bloomRadius.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        pipelineRenderer.setPipelineProperty("Bloom Radius", bloomRadius.getValue());
                    }
                });

        final Slider minimalBrightness = new Slider(0, 1, 0.01f, false, skin);
        minimalBrightness.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        pipelineRenderer.setPipelineProperty("Min Brightness", minimalBrightness.getValue());
                    }
                });

        final Slider bloomStrength = new Slider(0, 1, 0.01f, false, skin);
        bloomStrength.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        pipelineRenderer.setPipelineProperty("Bloom Strength", bloomStrength.getValue());
                    }
                });

        Table tbl = new Table();
        tbl.add(new Label("Bloom Radius", skin));
        tbl.add(bloomRadius).row();
        tbl.add(new Label("Min Brightness", skin));
        tbl.add(minimalBrightness).row();
        tbl.add(new Label("Bloom Strength", skin));
        tbl.add(bloomStrength).row();
        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
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
                System.out.println("Reloading...");
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
            InputStream stream = Gdx.files.local("pipeline.json").read();
            try {
                PipelineRenderer pipelineRenderer = PipelineLoader.loadPipeline(stream, new RendererLoaderCallback());
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
        pipelineRenderer.setPipelineProperty("Stage", stage);
        pipelineRenderer.setPipelineProperty("Models", models);
        pipelineRenderer.setPipelineProperty("Lights", environment);
        pipelineRenderer.setPipelineProperty("Camera", camera);
    }
}