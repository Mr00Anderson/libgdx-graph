package com.gempukku.graph.pipeline.producer.participant;

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

public class StartGraphBoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("PipelineStart");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();

        GraphBoxImpl start = new GraphBoxImpl(id, "PipelineStart", "Start", skin);
        start.setPosition(x, y);
        start.addBottomConnector(id + ":output");
        start.addGraphBoxPart(createColorPart(skin, id + ":background"));

        return start;
    }

    @Override
    public GraphBox createDefault(Skin skin, float x, float y) {
        return null;
    }

    private GraphBoxPartImpl createColorPart(Skin skin, String id) {
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new Label("Background color", skin));

        GraphBoxPartImpl colorPart = new GraphBoxPartImpl(horizontalGroup, null);
        colorPart.addConnector(id, GraphBoxConnector.Side.Left, GraphBoxConnector.CommunicationType.Input, PropertyType.Color);
        return colorPart;
    }
}
