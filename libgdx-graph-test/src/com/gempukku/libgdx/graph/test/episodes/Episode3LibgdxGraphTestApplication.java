package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
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
import com.gempukku.libgdx.graph.GraphLoader;
import com.gempukku.libgdx.graph.libgdx.LibGDXModels;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.pipeline.RendererLoaderCallback;
import com.gempukku.libgdx.graph.test.WhitePixel;

import java.io.IOException;
import java.io.InputStream;

public class Episode3LibgdxGraphTestApplication extends ApplicationAdapter {
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
        stage = constructStage();

        models = createModels();

        environment = createEnvironment();

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();

        Gdx.input.setInputProcessor(stage);
    }

    private Camera createCamera() {
        Camera camera = new PerspectiveCamera();
        camera.position.set(3f, 3f, 3f);
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
                new Material(TextureAttribute.createDiffuse(WhitePixel.texture), ColorAttribute.createDiffuse(new Color(0.5f, 0.5f, 0.5f, 1f))),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        LibGDXModels models = new LibGDXModels();
        models.addRenderableProvider(new ModelInstance(sphereModel));
        return models;
    }

    private Stage constructStage() {
        Stage stage = new Stage(new ScreenViewport());

        final Slider bloomRadius = new Slider(0, 64, 1, false, skin);
        bloomRadius.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        setPropertyIfDefined("Bloom Radius", bloomRadius.getValue());
                    }
                });

        final Slider minimalBrightness = new Slider(0, 1, 0.01f, false, skin);
        minimalBrightness.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        setPropertyIfDefined("Min Brightness", minimalBrightness.getValue());
                    }
                });

        final Slider bloomStrength = new Slider(0, 10, 0.01f, false, skin);
        bloomStrength.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        setPropertyIfDefined("Bloom Strength", bloomStrength.getValue());
                    }
                });

        final Slider blurRadius = new Slider(0, 64, 1f, false, skin);
        blurRadius.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        setPropertyIfDefined("Blur Radius", blurRadius.getValue());
                    }
                });

        final Slider gammaCorrection = new Slider(0, 5, 0.01f, false, skin);
        gammaCorrection.setValue(1f);
        gammaCorrection.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        setPropertyIfDefined("Gamma Correction", gammaCorrection.getValue());
                    }
                });

        Table tbl = new Table();
        tbl.add(new Label("Bloom Radius", skin));
        tbl.add(bloomRadius).row();
        tbl.add(new Label("Min Brightness", skin));
        tbl.add(minimalBrightness).row();
        tbl.add(new Label("Bloom Strength", skin));
        tbl.add(bloomStrength).row();
        tbl.add().height(10).colspan(2).row();
        tbl.add(new Label("Blur Radius", skin));
        tbl.add(blurRadius).row();
        tbl.add().height(10).colspan(2).row();
        tbl.add(new Label("Gamma Correction", skin));
        tbl.add(gammaCorrection).row();
        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
    }

    private void setPropertyIfDefined(String propertyName, float value) {
        if (pipelineRenderer.hasPipelineProperty(propertyName))
            pipelineRenderer.setPipelineProperty(propertyName, value);
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
            InputStream stream = Gdx.files.local("episodes/episode3.json").read();
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
        pipelineRenderer.setPipelineProperty("Stage", stage);
        pipelineRenderer.setPipelineProperty("Models", models);
        pipelineRenderer.setPipelineProperty("Lights", environment);
        pipelineRenderer.setPipelineProperty("Camera", camera);
    }
}