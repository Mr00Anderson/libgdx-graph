package com.gempukku.libgdx.graph.ui.producer.provided;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.pipeline.PropertyType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import org.json.simple.JSONObject;

public class TimeBoxProducer implements GraphBoxProducer {
    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Time";
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("Time");
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
        GraphBoxImpl end = new GraphBoxImpl(id, "Time", skin);
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
