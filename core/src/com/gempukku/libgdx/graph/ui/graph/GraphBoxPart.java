package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.graph.data.FieldType;
import org.json.simple.JSONObject;

public interface GraphBoxPart<T extends FieldType> {
    Actor getActor();

    GraphBoxOutputConnector<T> getOutputConnector();

    GraphBoxInputConnector<T> getInputConnector();

    void serializePart(JSONObject object);
}
