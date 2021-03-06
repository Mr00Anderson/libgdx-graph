package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureArray;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.WhitePixel;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.file.FileChooser;

public class LibgdxGraphApplication extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;

    private LibgdxGraphScreen libgdxGraphScreen;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        VisUI.load();
        WhitePixel.initializeShared();
        ;
        FileChooser.setDefaultPrefsName("com.gempukku.libgdx.graph.ui.filechooser");

        skin = new Skin(VisUI.SkinScale.X1.getSkinFile());//Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        libgdxGraphScreen = new LibgdxGraphScreen(skin);
        stage.addActor(libgdxGraphScreen);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        stage.act();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        stage.draw();
    }

    @Override
    public void dispose() {
        libgdxGraphScreen.dispose();
        skin.dispose();
        stage.dispose();

        WhitePixel.disposeShared();
        VisUI.dispose();

        Gdx.app.debug("Unclosed", Cubemap.getManagedStatus());
        Gdx.app.debug("Unclosed", GLFrameBuffer.getManagedStatus());
        Gdx.app.debug("Unclosed", Mesh.getManagedStatus());
        Gdx.app.debug("Unclosed", Texture.getManagedStatus());
        Gdx.app.debug("Unclosed", TextureArray.getManagedStatus());
        Gdx.app.debug("Unclosed", ShaderProgram.getManagedStatus());
    }
}
