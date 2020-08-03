package com.gempukku.graph.pipeline.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PipelineParticipant;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import org.json.simple.JSONObject;

public class ObjectRendererParticipantProducer implements PipelineParticipantProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("ObjectRenderer");
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

        GraphBoxImpl start = new GraphBoxImpl(id, "ObjectRenderer", "Object Renderer", skin);
        start.setPosition(x, y);
        start.addTopConnector(id + ":input");
        start.addBottomConnector(id + ":output");

        return start;
    }
}
