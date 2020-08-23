package com.gempukku.libgdx.graph.ui.shader.property;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyDefaultBox;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.json.simple.JSONObject;

public class PropertyFloatBoxProducer implements PropertyBoxProducer<ShaderFieldType> {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Float");
    }

    @Override
    public PropertyBox<ShaderFieldType> createPropertyBox(Skin skin, String name, JSONObject jsonObject) {
        float x = ((Number) jsonObject.get("x")).floatValue();
        return createPropertyBoxDefault(skin, name, x);
    }

    @Override
    public PropertyBox<ShaderFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Float", (float) 0);
    }

    private PropertyBox<ShaderFieldType> createPropertyBoxDefault(Skin skin, String name, float v1) {
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
                        v1Input.fire(new GraphChangedEvent(false));
                    }
                });

        final Table table = new Table();
        table.add(new Label("X ", skin));
        table.add(v1Input).grow();

        return new PropertyBoxImpl<ShaderFieldType>(skin, "Float",
                name,
                ShaderFieldType.Float,
                new PropertyDefaultBox() {
                    @Override
                    public Actor getActor() {
                        return table;
                    }

                    @Override
                    public JSONObject serializeData() {
                        JSONObject result = new JSONObject();
                        result.put("x", Float.parseFloat(v1Input.getText()));
                        return result;
                    }
                });
    }
}
