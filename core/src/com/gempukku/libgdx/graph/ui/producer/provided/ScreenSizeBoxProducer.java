package com.gempukku.libgdx.graph.ui.producer.provided;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import org.json.simple.JSONObject;

public class ScreenSizeBoxProducer implements GraphBoxProducer {
    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Screen Size";
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("ScreenSize");
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
        GraphBoxImpl end = new GraphBoxImpl(id, "ScreenSize", skin);
        end.addOutputGraphPart(skin,
                id + ":size", "Size", PropertyType.Vector2);
        end.addOutputGraphPart(skin,
                id + ":width", "Width", PropertyType.Vector1);
        end.addOutputGraphPart(skin,
                id + ":height", "Height", PropertyType.Vector1);

        return end;
    }
}
