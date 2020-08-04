package com.gempukku.graph.pipeline.producer.provided;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
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
        end.addGraphBoxPart(createTimePart(skin, id + ":size", "Size", PropertyType.Vector2));
        end.addGraphBoxPart(createTimePart(skin, id + ":width", "Width", PropertyType.Vector1));
        end.addGraphBoxPart(createTimePart(skin, id + ":height", "Height", PropertyType.Vector1));

        return end;
    }

    private GraphBoxPartImpl createTimePart(Skin skin, String id, String text, PropertyType type) {
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new Label(text, skin));

        GraphBoxPartImpl timePart = new GraphBoxPartImpl(horizontalGroup, null);
        timePart.addConnector(id, GraphBoxConnector.Side.Right, GraphBoxConnector.CommunicationType.Output, type);
        return timePart;
    }
}
