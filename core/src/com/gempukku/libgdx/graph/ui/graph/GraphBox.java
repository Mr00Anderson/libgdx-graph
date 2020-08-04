package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.json.simple.JSONObject;

public interface GraphBox {
    Actor getActor();

    JSONObject serializeData();

    String getId();

    String getType();

    Iterable<GraphBoxInputConnector> getGraphBoxInputConnectors();

    Iterable<GraphBoxOutputConnector> getGraphBoxOutputConnectors();
}
