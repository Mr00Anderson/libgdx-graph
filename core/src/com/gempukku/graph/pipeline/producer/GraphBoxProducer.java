package com.gempukku.graph.pipeline.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import org.json.simple.JSONObject;

public interface GraphBoxProducer {
    boolean supportsType(String type);

    GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject);

    GraphBox createDefault(Skin skin, String id, float x, float y);
}
