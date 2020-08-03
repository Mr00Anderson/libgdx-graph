package com.gempukku.libgdx.graph.ui.preview;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.graph.ui.SleepingTab;

public class ScenePreviewTab extends SleepingTab {
    private Table contentTable;
    private Skin skin;

    public ScenePreviewTab(Skin skin) {
        super(false, false);

        this.skin = skin;
        contentTable = new Table(skin);
    }

    @Override
    public void sleep() {
        //TODO
    }

    @Override
    public void awaken() {
        //TODO
    }

    @Override
    public String getTabTitle() {
        return "Scene";
    }

    @Override
    public Table getContentTable() {
        return contentTable;
    }
}
