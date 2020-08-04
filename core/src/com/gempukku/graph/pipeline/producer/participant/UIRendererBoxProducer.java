package com.gempukku.graph.pipeline.producer.participant;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import org.json.simple.JSONObject;

public class UIRendererBoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("UIRenderer");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();

        GraphBoxImpl start = new GraphBoxImpl(id, "UIRenderer", "UI Renderer", skin);
        start.setPosition(x, y);
        start.addTopConnector(id + ":input");
        start.addBottomConnector(id + ":output");

        return start;
    }

    @Override
    public GraphBox createDefault(Skin skin, String id, float x, float y) {
        GraphBoxImpl start = new GraphBoxImpl(id, "UIRenderer", "UI Renderer", skin);
        start.setPosition(x, y);
        start.addTopConnector(id + ":input");
        start.addBottomConnector(id + ":output");

        return start;
    }
}
