package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.json.simple.JSONObject;

public interface PropertyBoxProducer {
    boolean supportsType(String type);

    PropertyBox createPropertyBox(Skin skin, String name, JSONObject jsonObject);

    PropertyBox createDefaultPropertyBox(Skin skin);
}
