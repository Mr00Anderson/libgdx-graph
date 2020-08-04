package com.gempukku.graph.pipeline.producer.provided;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import org.json.simple.JSONObject;

import java.util.UUID;

public class ScreenSizeBoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("ScreenSize");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();

        return createGraphBox(skin, id, x, y);
    }

    @Override
    public GraphBox createDefault(Skin skin, float x, float y) {
        String id = UUID.randomUUID().toString().replace("-", "");

        return createGraphBox(skin, id, x, y);
    }

    private GraphBox createGraphBox(Skin skin, String id, float x, float y) {
        GraphBoxImpl end = new GraphBoxImpl(id, "ScreenSize", "Screen Size", skin);
        end.setPosition(x, y);
        end.addOutputGraphPart(skin,
                id + ":size", "Size", PropertyType.Vector2);
        end.addOutputGraphPart(skin,
                id + ":width", "Width", PropertyType.Vector2);
        end.addOutputGraphPart(skin,
                id + ":height", "Height", PropertyType.Vector2);

        return end;
    }
}
