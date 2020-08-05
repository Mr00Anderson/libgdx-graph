package com.gempukku.libgdx.graph.ui.producer.part;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.google.common.base.Predicates;
import org.json.simple.JSONObject;

public class MergeBoxProducer implements GraphBoxProducer {
    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Merge";
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("Merge");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JSONObject data) {
        float x = ((Number) data.get("x")).floatValue();
        float y = ((Number) data.get("y")).floatValue();

        return createGraphBox(skin, id);
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        return createGraphBox(skin, id);
    }

    private GraphBox createGraphBox(Skin skin, String id) {
        GraphBoxImpl end = new GraphBoxImpl(id, "Merge", skin);
        end.addTwoSideGraphPart(skin,
                id + ":x", "X", Predicates.equalTo(PropertyType.Vector1),
                id + ":v2", "V2", PropertyType.Vector2);
        end.addTwoSideGraphPart(skin,
                id + ":y", "Y", Predicates.equalTo(PropertyType.Vector1),
                id + ":v3", "V3", PropertyType.Vector3);
        end.addTwoSideGraphPart(skin,
                id + ":z", "Z", Predicates.equalTo(PropertyType.Vector1),
                id + ":color", "Color", PropertyType.Color);
        end.addInputGraphPart(skin,
                id + ":w", "W", Predicates.equalTo(PropertyType.Vector1));
        return end;
    }
}
