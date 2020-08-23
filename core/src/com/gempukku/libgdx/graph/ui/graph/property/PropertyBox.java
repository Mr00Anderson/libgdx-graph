package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.graph.data.FieldType;
import org.json.simple.JSONObject;

public interface PropertyBox<T extends FieldType> extends PropertyProducer<T> {
    String getType();

    JSONObject serializeData();

    Actor getActor();
}
