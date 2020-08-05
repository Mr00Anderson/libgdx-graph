package com.gempukku.libgdx.graph.ui.producer.rendering;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import org.json.simple.JSONObject;

public class ObjectRendererBoxProducer implements GraphBoxProducer {
    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Default Renderer";
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("ObjectRenderer");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JSONObject jsonObject) {
        GraphBoxImpl start = new GraphBoxImpl(id, "ObjectRenderer", skin);
        start.addTopConnector(id + ":input");
        start.addBottomConnector(id + ":output");

        return start;
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        GraphBoxImpl start = new GraphBoxImpl(id, "ObjectRenderer", skin);
        start.addTopConnector(id + ":input");
        start.addBottomConnector(id + ":output");

        return start;
    }
}
