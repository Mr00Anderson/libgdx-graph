package com.gempukku.graph.pipeline.producer.participant;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import org.json.simple.JSONObject;

public class EndGraphBoxProducer implements GraphBoxProducer {
    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    public String getTitle() {
        return "End";
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("PipelineEnd");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JSONObject jsonObject) {
        GraphBoxImpl end = new GraphBoxImpl(id, "PipelineEnd", skin);
        end.addTopConnector(id + ":input");
        return end;
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        return null;
    }
}
