package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.data.FieldType;

public interface PropertyProducer<T extends FieldType> {
    String getName();

    GraphBox<T> createPropertyBox(Skin skin, String id, float x, float y);
}
