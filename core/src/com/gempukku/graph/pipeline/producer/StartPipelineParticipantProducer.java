package com.gempukku.graph.pipeline.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PipelineParticipant;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.part.ColorInputGraphBoxPart;
import org.json.simple.JSONObject;

public class StartPipelineParticipantProducer implements PipelineParticipantProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Start");
    }

    @Override
    public PipelineParticipant createPipelineParticipant(JSONObject jsonObject) {
        return null;
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();

        GraphBoxImpl start = new GraphBoxImpl(id, "Start", "Start", skin);
        start.setPosition(x, y);
        start.addBottomConnector(id + ":output");

        start.addGraphBoxPart(new ColorInputGraphBoxPart(skin, id + ":background"));

        return start;
    }
}
