package com.gempukku.libgdx.graph.test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.PipelineLoader;
import com.gempukku.libgdx.graph.renderer.PipelineRenderer;
import com.gempukku.libgdx.graph.renderer.RenderOutputs;
import com.gempukku.libgdx.graph.renderer.RendererLoaderCallback;

import java.io.IOException;
import java.io.InputStream;

public class LibgdxGraphTestApplication extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;

    private PipelineRenderer pipelineRenderer;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        pipelineRenderer = loadPipelineRenderer();

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        stage.act();

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void dispose() {
        pipelineRenderer.dispose();
        skin.dispose();
        stage.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        try {
            InputStream stream = Gdx.files.internal("pipeline/minimum.json").read();
            try {
                return PipelineLoader.loadPipeline(stream, new RendererLoaderCallback());
            } finally {
                stream.close();
            }
        } catch (IOException exp) {
            throw new RuntimeException("Unable to load pipeline", exp);
        }
    }
}