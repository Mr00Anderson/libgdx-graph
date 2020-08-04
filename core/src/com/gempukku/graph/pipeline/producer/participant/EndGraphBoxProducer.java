package com.gempukku.graph.pipeline.producer.participant;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import org.json.simple.JSONObject;

public class EndGraphBoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("PipelineEnd");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();

        GraphBoxImpl end = new GraphBoxImpl(id, "PipelineEnd", "End", skin);
        end.setPosition(x, y);
        end.addTopConnector(id + ":input");
        return end;
    }

    @Override
    public GraphBox createDefault(Skin skin, String id, float x, float y) {
        return null;
    }
}
