package com.gempukku.libgdx.graph;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.ui.SleepingTab;
import com.gempukku.libgdx.graph.ui.pipeline.PipelineDesignTab;
import com.gempukku.libgdx.graph.ui.preview.ScenePreviewTab;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;

public class LibgdxGraph extends ApplicationAdapter {
    private Stage stage;
    private Skin skin;

    private Table mainTable;
    private Table insideTable;
    private PipelineDesignTab pipelineDesignTab;
    private ScenePreviewTab scenePreviewTab;

    @Override
    public void create() {
        VisUI.load();

        skin = new Skin(Gdx.files.internal("uiskin.json"));//skin/metal/metal-ui.json"));
        stage = new Stage(new ScreenViewport());

        mainTable = new Table(skin);
        mainTable.setFillParent(true);
        insideTable = new Table(skin);

        final TabbedPane tabbedPane = new TabbedPane();
        tabbedPane.addListener(
                new TabbedPaneAdapter() {
                    @Override
                    public void switchedTab(Tab tab) {
                        ((SleepingTab) tabbedPane.getActiveTab()).sleep();
                        ((SleepingTab) tabbedPane.getActiveTab()).awaken();

                        insideTable.clearChildren();
                        insideTable.add(tab.getContentTable()).grow().row();
                    }
                }
        );

        pipelineDesignTab = new PipelineDesignTab(skin);
        scenePreviewTab = new ScenePreviewTab(skin);
        tabbedPane.add(pipelineDesignTab);
        tabbedPane.add(scenePreviewTab);
        tabbedPane.switchTab(0);

        pipelineDesignTab.awaken();

        mainTable.add(tabbedPane.getTable()).left().growX().row();
        mainTable.add(insideTable).grow().row();

        stage.addActor(mainTable);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        pipelineDesignTab.resize(width, height);
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
        pipelineDesignTab.dispose();
        scenePreviewTab.dispose();
        skin.dispose();
        stage.dispose();
    }
}
