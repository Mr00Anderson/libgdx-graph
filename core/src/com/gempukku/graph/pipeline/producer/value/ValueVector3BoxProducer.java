package com.gempukku.graph.pipeline.producer.value;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.json.simple.JSONObject;

import java.util.UUID;

public class ValueVector3BoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("ValueVector3");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();
        float v1 = ((Number) jsonObject.get("v1")).floatValue();
        float v2 = ((Number) jsonObject.get("v2")).floatValue();
        float v3 = ((Number) jsonObject.get("v3")).floatValue();

        return createGraphBox(skin, id, x, y, v1, v2, v3);
    }

    @Override
    public GraphBox createDefault(Skin skin, float x, float y) {
        String id = UUID.randomUUID().toString().replace("-", "");

        return createGraphBox(skin, id, x, y, 0, 0, 0);
    }

    private GraphBox createGraphBox(Skin skin, String id, float x, float y, float v1, float v2, float v3) {
        GraphBoxImpl end = new GraphBoxImpl(id, "ValueVector3", "Vector3", skin);
        end.setPosition(x, y);
        end.addGraphBoxPart(createValuePart(skin, id, v1, v2, v3));

        return end;
    }

    private GraphBoxPartImpl createValuePart(Skin skin, String id, float v1, float v2, float v3) {
        final VisValidatableTextField v1Input = new VisValidatableTextField(new Validators.FloatValidator()) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v1Input.setText(String.valueOf(v1));
        final VisValidatableTextField v2Input = new VisValidatableTextField(new Validators.FloatValidator()) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v2Input.setText(String.valueOf(v2));
        final VisValidatableTextField v3Input = new VisValidatableTextField(new Validators.FloatValidator()) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v3Input.setText(String.valueOf(v3));

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new Label("x", skin));
        horizontalGroup.addActor(v1Input);
        horizontalGroup.addActor(new Label("y", skin));
        horizontalGroup.addActor(v2Input);
        horizontalGroup.addActor(new Label("z", skin));
        horizontalGroup.addActor(v3Input);

        GraphBoxPartImpl colorPart = new GraphBoxPartImpl(horizontalGroup,
                new GraphBoxPartImpl.Callback() {
                    @Override
                    public void serialize(JSONObject object) {
                        object.put("v1", Float.parseFloat(v1Input.getText()));
                        object.put("v2", Float.parseFloat(v2Input.getText()));
                        object.put("v3", Float.parseFloat(v3Input.getText()));
                    }
                });
        colorPart.addConnector(id, GraphBoxConnector.Side.Right, GraphBoxConnector.CommunicationType.Output, PropertyType.Vector3);
        return colorPart;
    }
}
