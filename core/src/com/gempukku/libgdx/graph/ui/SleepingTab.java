package com.gempukku.libgdx.graph.ui;

import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

public abstract class SleepingTab extends Tab {
    public SleepingTab() {
    }

    public SleepingTab(boolean savable) {
        super(savable);
    }

    public SleepingTab(boolean savable, boolean closeableByUser) {
        super(savable, closeableByUser);
    }

    public abstract void sleep();

    public abstract void awaken();
}
