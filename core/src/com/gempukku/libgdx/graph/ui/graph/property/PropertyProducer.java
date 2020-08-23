package com.gempukku.libgdx.graph.ui.graph.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;

public interface PropertyProducer<T extends FieldType> {
    String getName();

    GraphBox<T> createPropertyBox(Skin skin, String id, float x, float y);
}
