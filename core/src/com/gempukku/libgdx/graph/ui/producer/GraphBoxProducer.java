package com.gempukku.libgdx.graph.ui.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import org.json.simple.JSONObject;

public interface GraphBoxProducer {
    String getType();

    boolean isCloseable();

    String getTitle();

    GraphBox createPipelineGraphBox(Skin skin, String id, JSONObject data);

    GraphBox createDefault(Skin skin, String id);
}
