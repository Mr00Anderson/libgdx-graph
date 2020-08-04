package com.gempukku.graph.pipeline.producer.part;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.google.common.base.Predicates;
import org.json.simple.JSONObject;

public class MergeBoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Merge");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();

        return createGraphBox(skin, id, x, y);
    }

    @Override
    public GraphBox createDefault(Skin skin, String id, float x, float y) {
        return createGraphBox(skin, id, x, y);
    }

    private GraphBox createGraphBox(Skin skin, String id, float x, float y) {
        GraphBoxImpl end = new GraphBoxImpl(id, "Merge", "Merge", skin);
        end.setPosition(x, y);
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
