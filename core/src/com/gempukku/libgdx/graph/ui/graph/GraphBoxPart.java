package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.json.simple.JSONObject;

public interface GraphBoxPart {
    Actor getActor();

    Iterable<? extends GraphBoxConnector> getConnectors();

    void serializePart(JSONObject object);
}
