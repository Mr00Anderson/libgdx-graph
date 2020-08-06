package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBox;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyDefaultBox;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.json.simple.JSONObject;

public class PropertyVector1BoxProducer implements PropertyBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Vector1");
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, JSONObject jsonObject) {
        float x = ((Number) jsonObject.get("x")).floatValue();
        return createPropertyBoxDefault(skin, name, x);
    }

    @Override
    public PropertyBox createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Vector1", (float) 0);
    }

    private PropertyBox createPropertyBoxDefault(Skin skin, String name, float v1) {
        final VisValidatableTextField v1Input = new VisValidatableTextField(new Validators.FloatValidator()) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v1Input.setText(String.valueOf(v1));

        final Table table = new Table();
        table.add(new Label("Default: ", skin));
        table.add(new Label("X", skin));
        table.add(v1Input).grow();

        return new PropertyBoxImpl(skin, "Vector1",
                name,
                PropertyType.Vector1,
                new PropertyDefaultBox() {
                    @Override
                    public Actor getActor() {
                        return table;
                    }

                    @Override
                    public JSONObject serializeDefault() {
                        JSONObject result = new JSONObject();
                        result.put("x", Float.parseFloat(v1Input.getText()));
                        return result;
                    }
                });
    }
}
