package com.gempukku.graph.pipeline.producer.value;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.part.ColorOutputGraphBoxPart;
import org.json.simple.JSONObject;

import java.util.UUID;

public class ValueColorBoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("ValueColor");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();

        GraphBoxImpl end = new GraphBoxImpl(id, "ValueColor", "Color", skin);
        end.setPosition(x, y);
        end.addGraphBoxPart(new ColorOutputGraphBoxPart(skin, "color", "color", jsonObject.get("color")));

        return end;
    }

    @Override
    public GraphBox createDefault(Skin skin, float x, float y) {
        String randomId = UUID.randomUUID().toString().replace("-", "");

        GraphBoxImpl end = new GraphBoxImpl(randomId, "ValueColor", "Color", skin);
        end.setPosition(x, y);
        end.addGraphBoxPart(new ColorOutputGraphBoxPart(skin, "color", "color"));

        return end;
    }
}
