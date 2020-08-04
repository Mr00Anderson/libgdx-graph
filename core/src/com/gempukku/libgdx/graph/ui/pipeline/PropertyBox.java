package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.graph.ui.graph.PropertyProducer;
import org.json.simple.JSONObject;

public interface PropertyBox extends PropertyProducer {
    JSONObject serializeProperty();

    Actor getActor();
}
