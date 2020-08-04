package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.graph.pipeline.PropertyType;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBox;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyDefaultBox;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.json.simple.JSONObject;

public class PropertyVector2BoxProducer implements PropertyBoxProducer {
    private Actor actor;

    @Override
    public boolean supportsType(String type) {
        return type.equals("Vector1");
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, JSONObject jsonObject) {
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();
        return createPropertyBoxDefault(skin, name, x, y);
    }

    @Override
    public PropertyBox createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Vector2", 0f, 0f);
    }

    private PropertyBox createPropertyBoxDefault(Skin skin, String name, float v1, float v2) {
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

        final Table table = new Table();
        table.add(new Label("Default: ", skin));
        table.add(new Label("X", skin));
        table.add(v1Input).grow();
        table.add(new Label("Y", skin));
        table.add(v2Input).grow();

        return new PropertyBoxImpl(skin, "Vector2",
                name,
                PropertyType.Vector2,
                new PropertyDefaultBox() {
                    @Override
                    public Actor getActor() {
                        return table;
                    }

                    @Override
                    public JSONObject serializeDefault() {
                        JSONObject result = new JSONObject();
                        result.put("x", Float.parseFloat(v1Input.getText()));
                        result.put("y", Float.parseFloat(v2Input.getText()));
                        return result;
                    }
                });
    }
}
