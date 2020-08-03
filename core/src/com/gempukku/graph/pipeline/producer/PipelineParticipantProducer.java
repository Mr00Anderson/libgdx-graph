package com.gempukku.graph.pipeline.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PipelineParticipant;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import org.json.simple.JSONObject;

public interface PipelineParticipantProducer {
    boolean supportsType(String type);

    PipelineParticipant createPipelineParticipant(JSONObject jsonObject);

    GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject);
}
