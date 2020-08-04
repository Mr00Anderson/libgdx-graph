package com.gempukku.graph.pipeline.producer.provided;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import org.json.simple.JSONObject;

public class TimeBoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Time");
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
        GraphBoxImpl end = new GraphBoxImpl(id, "Time", "Time", skin);
        end.setPosition(x, y);
        end.addOutputGraphPart(skin,
                id + ":time", "Time", PropertyType.Vector1);
        end.addOutputGraphPart(skin,
                id + ":sinTime", "sin(Time)", PropertyType.Vector1);
        end.addOutputGraphPart(skin,
                id + ":cosTime", "cos(Time)", PropertyType.Vector1);
        end.addOutputGraphPart(skin,
                id + ":deltaTime", "Delta", PropertyType.Vector1);

        return end;
    }
}
