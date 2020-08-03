package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Event;

public class GraphChangedEvent extends Event {
    public static final GraphChangedEvent INSTANCE = new GraphChangedEvent();

    private GraphChangedEvent() {

    }
}
