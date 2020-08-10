package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;

public class GraphChangedEvent extends Event {
    private boolean structural;

    public GraphChangedEvent(boolean structural) {
        this.structural = structural;
    }

    public boolean isStructural() {
        return structural;
    }
}
