package com.gempukku.graph.pipeline.producer.participant;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.part.ColorInputGraphBoxPart;
import org.json.simple.JSONObject;

public class StartGraphBoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("PipelineStart");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();

        GraphBoxImpl start = new GraphBoxImpl(id, "PipelineStart", "Start", skin);
        start.setPosition(x, y);
        start.addBottomConnector(id + ":output");

        start.addGraphBoxPart(new ColorInputGraphBoxPart(skin, id + ":background"));

        return start;
    }

    @Override
    public GraphBox createDefault(Skin skin, float x, float y) {
        return null;
    }
}
