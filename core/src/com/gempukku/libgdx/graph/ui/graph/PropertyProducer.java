package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public interface PropertyProducer {
    String getName();

    GraphBox createPropertyBox(Skin skin, String id, float x, float y);
}
