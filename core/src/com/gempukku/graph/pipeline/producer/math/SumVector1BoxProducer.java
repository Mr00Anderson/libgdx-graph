package com.gempukku.graph.pipeline.producer.math;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import org.json.simple.JSONObject;

public class SumVector1BoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return false;
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        return null;
    }

    @Override
    public GraphBox createDefault(Skin skin, String id, float x, float y) {
        return null;
    }
}
