package com.gempukku.libgdx.graph.ui.producer.postprocessor;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.google.common.base.Predicates;
import org.json.simple.JSONObject;

public class BloomPostProcessorBoxProducer implements GraphBoxProducer {
    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Bloom";
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("Bloom");
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
        GraphBoxImpl start = new GraphBoxImpl(id, "Bloom", skin);
        start.addTopConnector(id + ":input");
        start.addBottomConnector(id + ":output");
        start.addInputGraphPart(skin, id + ":bloomRadius", "Bloom Radius",
                Predicates.equalTo(PropertyType.Vector1));
        start.addInputGraphPart(skin, id + ":minimalBrightness", "Min Brightness",
                Predicates.equalTo(PropertyType.Vector1));
        start.addInputGraphPart(skin, id + ":bloomStrength", "Bloom Strength",
                Predicates.equalTo(PropertyType.Vector1));

        return start;
    }
}
