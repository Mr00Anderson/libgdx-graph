package com.gempukku.libgdx.graph.ui.producer.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.google.common.base.Predicates;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.json.simple.JSONObject;

public class ValueVector2BoxProducer implements GraphBoxProducer {
    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Vector2";
    }

    @Override
    public String getType() {
        return "ValueVector2";
    }


    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JSONObject data) {
        float v1 = ((Number) data.get("v1")).floatValue();
        float v2 = ((Number) data.get("v2")).floatValue();

        return createGraphBox(skin, id, v1, v2);
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        return createGraphBox(skin, id, 0, 0);
    }

    private GraphBox createGraphBox(Skin skin, String id, float v1, float v2) {
        GraphBoxImpl end = new GraphBoxImpl(id, "ValueVector2", skin);
        end.addGraphBoxPart(createValuePart(skin, id + ":value", v1, v2));

        return end;
    }

    private GraphBoxPartImpl createValuePart(Skin skin, String id, float v1, float v2) {
        final VisValidatableTextField v1Input = new VisValidatableTextField(new Validators.FloatValidator()) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v1Input.setText(String.valueOf(v1));
        v1Input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        v1Input.fire(new GraphChangedEvent());
                    }
                });

        final VisValidatableTextField v2Input = new VisValidatableTextField(new Validators.FloatValidator()) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v2Input.setText(String.valueOf(v2));
        v2Input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        v2Input.fire(new GraphChangedEvent());
                    }
                });

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new Label("x", skin));
        horizontalGroup.addActor(v1Input);
        horizontalGroup.addActor(new Label("y", skin));
        horizontalGroup.addActor(v2Input);

        GraphBoxPartImpl colorPart = new GraphBoxPartImpl(horizontalGroup,
                new GraphBoxPartImpl.Callback() {
                    @Override
                    public void serialize(JSONObject object) {
                        object.put("v1", Float.parseFloat(v1Input.getText()));
                        object.put("v2", Float.parseFloat(v2Input.getText()));
                    }
                });
        colorPart.setOutputConnector(id, GraphBoxOutputConnector.Side.Right, Predicates.equalTo(PropertyType.Vector2));
        return colorPart;
    }
}
