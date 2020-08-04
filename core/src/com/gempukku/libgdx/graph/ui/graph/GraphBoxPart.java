package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.json.simple.JSONObject;

public interface GraphBoxPart {
    Actor getActor();

    GraphBoxOutputConnector getOutputConnector();

    GraphBoxInputConnector getInputConnector();

    void serializePart(JSONObject object);
}
