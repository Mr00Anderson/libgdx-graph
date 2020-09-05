package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
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
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstance;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModels;
import com.gempukku.libgdx.graph.test.WhitePixel;

import java.io.IOException;
import java.io.InputStream;

public class Episode5LibgdxGraphTestApplication extends ApplicationAdapter {
    private long lastProcessedInput;

    private PipelineRenderer pipelineRenderer;
    private GraphShaderModels models;
    private Model sphereModel;
    private GraphShaderModelInstance sphereModelInstance;
    private Camera camera;
    private Texture rockTexture;
    private Stage stage;
    private Skin skin;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        WhitePixel.initialize();

        rockTexture = new Texture(Gdx.files.internal("image/seamless_rock_face_texture_by_hhh316.jpg"));
        ModelBuilder modelBuilder = new ModelBuilder();
        sphereModel = modelBuilder.createSphere(1, 1, 1, 20, 20,
                new Material(TextureAttribute.createDiffuse(rockTexture)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = createStage();

        models = createModels();
        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();

        Gdx.input.setInputProcessor(stage);
    }

    private Camera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;
        camera.position.set(1f, 0.5f, 0f);
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0f, 0f);
        camera.update();
        return camera;
    }

    private GraphShaderModels createModels() {
        GraphShaderModels models = new GraphShaderModels();
        String modelId = models.registerModel(sphereModel);
        sphereModelInstance = models.createModelInstance(modelId);
        sphereModelInstance.addTag("Cover");
        return models;
    }

    private Stage createStage() {
        Stage stage = new Stage(new ScreenViewport());

        final Slider amount = new Slider(0, 10, 0.1f, false, skin);
        amount.setValue(1f);
        amount.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        sphereModelInstance.setProperty("Amount", amount.getValue());
                    }
                });
        final Slider scale = new Slider(1, 50, 1f, false, skin);
        scale.setValue(20f);
        scale.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        sphereModelInstance.setProperty("Scale", scale.getValue());
                    }
                });
        final Slider x = new Slider(-1, 1, 0.01f, false, skin);
        x.setValue(0f);
        final Slider y = new Slider(-1, 1, 0.01f, false, skin);
        y.setValue(1f);
        final Slider z = new Slider(-1, 1, 0.01f, false, skin);
        z.setValue(0f);
        ChangeListener directionListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                sphereModelInstance.setProperty("Direction", new Vector3(x.getValue(), y.getValue(), z.getValue()));
            }
        };
        x.addListener(directionListener);
        y.addListener(directionListener);
        z.addListener(directionListener);


        Table tbl = new Table();
        tbl.add(new Label("Cover amount", skin));
        tbl.add(amount).row();
        tbl.add(new Label("Cover scale", skin));
        tbl.add(scale).row();
        tbl.add(new Label("X", skin));
        tbl.add(x).row();
        tbl.add(new Label("Y", skin));
        tbl.add(y).row();
        tbl.add(new Label("Z", skin));
        tbl.add(z).row();

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
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
        skin.dispose();
        stage.dispose();
        sphereModel.dispose();
        pipelineRenderer.dispose();
        rockTexture.dispose();
        WhitePixel.dispose();

        Gdx.app.debug("Unclosed", Cubemap.getManagedStatus());
        Gdx.app.debug("Unclosed", GLFrameBuffer.getManagedStatus());
        Gdx.app.debug("Unclosed", Mesh.getManagedStatus());
        Gdx.app.debug("Unclosed", Texture.getManagedStatus());
        Gdx.app.debug("Unclosed", TextureArray.getManagedStatus());
        Gdx.app.debug("Unclosed", ShaderProgram.getManagedStatus());
    }

    private PipelineRenderer loadPipelineRenderer() {
        try {
            InputStream stream = Gdx.files.local("episodes/episode5.json").read();
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
        pipelineRenderer.setPipelineProperty("Models", models);
        pipelineRenderer.setPipelineProperty("Camera", camera);
        pipelineRenderer.setPipelineProperty("Stage", stage);
    }
}