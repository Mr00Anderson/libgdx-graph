package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.scenes.scene2d.Actor;
import org.json.simple.JSONObject;

public interface PropertyDefaultBox {
    Actor getActor();

    void serializeDefault(JSONObject result);
}
