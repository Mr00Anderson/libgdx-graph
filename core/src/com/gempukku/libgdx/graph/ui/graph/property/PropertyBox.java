package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import org.json.simple.JSONObject;

public interface PropertyBox<T extends FieldType> extends GraphProperty<T> {
    JSONObject serializeData();

    Actor getActor();

    GraphBox<T> createPropertyBox(Skin skin, String id, float x, float y);
}
