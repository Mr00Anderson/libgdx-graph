package com.gempukku.graph.pipeline.producer.value;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import org.json.simple.JSONObject;

import java.util.UUID;

public class ValueBooleanBoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("ValueBool");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();
        boolean v = (Boolean) jsonObject.get("v");

        return createGraphBox(skin, id, x, y, v);
    }

    @Override
    public GraphBox createDefault(Skin skin, float x, float y) {
        String id = UUID.randomUUID().toString().replace("-", "");

        return createGraphBox(skin, id, x, y, false);
    }

    private GraphBox createGraphBox(Skin skin, String id, float x, float y, boolean v) {
        GraphBoxImpl end = new GraphBoxImpl(id, "ValueBool", "Boolean", skin);
        end.setPosition(x, y);
        end.addGraphBoxPart(createValuePart(skin, id, v));

        return end;
    }

    private GraphBoxPartImpl createValuePart(Skin skin, String id, boolean v) {

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        final CheckBox checkBox = new CheckBox("Value", skin);
        checkBox.setChecked(v);
        horizontalGroup.addActor(checkBox);

        GraphBoxPartImpl colorPart = new GraphBoxPartImpl(horizontalGroup,
                new GraphBoxPartImpl.Callback() {
                    @Override
                    public void serialize(JSONObject object) {
                        object.put("v", checkBox.isChecked());
                    }
                });
        colorPart.addConnector(id, GraphBoxConnector.Side.Right, GraphBoxConnector.CommunicationType.Output, PropertyType.Boolean);
        return colorPart;
    }
}
