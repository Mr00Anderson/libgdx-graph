package com.gempukku.graph.pipeline.producer.value;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.json.simple.JSONObject;

import java.util.UUID;

public class ValueVector1BoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("ValueVector1");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();
        float v1 = ((Number) jsonObject.get("v1")).floatValue();

        return createGraphBox(skin, id, x, y, v1);
    }

    @Override
    public GraphBox createDefault(Skin skin, float x, float y) {
        String id = UUID.randomUUID().toString().replace("-", "");

        return createGraphBox(skin, id, x, y, 0);
    }

    private GraphBox createGraphBox(Skin skin, String id, float x, float y, float v1) {
        GraphBoxImpl end = new GraphBoxImpl(id, "ValueVector1", "Vector1", skin);
        end.setPosition(x, y);
        end.addGraphBoxPart(createValuePart(skin, id, v1));

        return end;
    }

    private GraphBoxPartImpl createValuePart(Skin skin, String id, float v1) {
        final VisValidatableTextField v1Input = new VisValidatableTextField(new Validators.FloatValidator()) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v1Input.setText(String.valueOf(v1));

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new Label("x", skin));
        horizontalGroup.addActor(v1Input);

        GraphBoxPartImpl colorPart = new GraphBoxPartImpl(horizontalGroup,
                new GraphBoxPartImpl.Callback() {
                    @Override
                    public void serialize(JSONObject object) {
                        object.put("v1", Float.parseFloat(v1Input.getText()));
                    }
                });
        colorPart.setOutputConnector(id, GraphBoxOutputConnector.Side.Right, PropertyType.Vector1);
        return colorPart;
    }
}
