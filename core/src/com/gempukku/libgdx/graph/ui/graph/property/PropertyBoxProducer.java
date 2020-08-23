package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.data.FieldType;
import org.json.simple.JSONObject;

public interface PropertyBoxProducer<T extends FieldType> {
    boolean supportsType(String type);

    PropertyBox<T> createPropertyBox(Skin skin, String name, JSONObject jsonObject);

    PropertyBox<T> createDefaultPropertyBox(Skin skin);
}
