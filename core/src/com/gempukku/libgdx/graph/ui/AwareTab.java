package com.gempukku.libgdx.graph.ui;

import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

public abstract class AwareTab extends Tab {
    public AwareTab() {
    }

    public AwareTab(boolean savable) {
        super(savable);
    }

    public AwareTab(boolean savable, boolean closeableByUser) {
        super(savable, closeableByUser);
    }

    public abstract void resized(int width, int height);

    public abstract void sleep();

    public abstract void awaken();
}
