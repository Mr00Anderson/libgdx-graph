package com.gempukku.libgdx.graph.ui.producer.rendering;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.google.common.base.Predicates;
import org.json.simple.JSONObject;

public class UIRendererBoxProducer implements GraphBoxProducer {
    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getTitle() {
        return "UI Renderer";
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("UIRenderer");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JSONObject jsonObject) {
        return createGraphBox(skin, id);
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        return createGraphBox(skin, id);
    }

    private GraphBox createGraphBox(Skin skin, String id) {
        GraphBoxImpl start = new GraphBoxImpl(id, "UIRenderer", skin);
        start.addTopConnector(id + ":input");
        start.addBottomConnector(id + ":output");
        start.addInputGraphPart(
                skin, id + ":camera", "Camera",
                Predicates.equalTo(PropertyType.Camera));

        return start;
    }
}
