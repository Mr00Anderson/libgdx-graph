package com.gempukku.libgdx.graph.ui.producer.value;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.pipeline.PropertyType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import org.json.simple.JSONObject;

public class ValueBooleanBoxProducer implements GraphBoxProducer {
    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Boolean";
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("ValueBool");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JSONObject data) {
        boolean v = (Boolean) data.get("v");

        return createGraphBox(skin, id, v);
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        return createGraphBox(skin, id, false);
    }

    private GraphBox createGraphBox(Skin skin, String id, boolean v) {
        GraphBoxImpl end = new GraphBoxImpl(id, "ValueBool", skin);
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
        colorPart.setOutputConnector(id, GraphBoxOutputConnector.Side.Right, PropertyType.Boolean);
        return colorPart;
    }
}
