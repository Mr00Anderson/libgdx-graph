package com.gempukku.graph.pipeline.producer.part;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.google.common.base.Predicates;
import org.json.simple.JSONObject;

import java.util.Arrays;

public class SplitBoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Split");
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
        GraphBoxImpl end = new GraphBoxImpl(id, "Split", "Split", skin);
        end.setPosition(x, y);
        end.addTwoSideGraphPart(skin,
                id + ":input", "Input", Predicates.in(Arrays.asList(PropertyType.Vector2, PropertyType.Vector3, PropertyType.Color)),
                id + ":x", "X", PropertyType.Vector1);
        end.addOutputGraphPart(skin,
                id + ":y", "Y", PropertyType.Vector1);
        end.addOutputGraphPart(skin,
                id + ":z", "Z", PropertyType.Vector1);
        end.addOutputGraphPart(skin,
                id + ":w", "W", PropertyType.Vector1);
        return end;
    }
}
