package com.gempukku.libgdx.graph.ui.shader.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.json.simple.JSONObject;

public class IndexBoxPart<T extends FieldType> extends Table implements GraphBoxPart<T> {
    private final VisValidatableTextField indexField;

    private String property;

    public IndexBoxPart(Skin skin, String label, String property) {
        super(skin);
        this.property = property;

        indexField = new VisValidatableTextField(Validators.INTEGERS, new Validators.GreaterThanValidator(0, true));
        indexField.setText("0");
        add(new Label(label + " ", skin));
        add(indexField).growX();
        row();

        indexField.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        fire(new GraphChangedEvent(false, true));
                    }
                });
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBoxOutputConnector<T> getOutputConnector() {
        return null;
    }

    @Override
    public GraphBoxInputConnector<T> getInputConnector() {
        return null;
    }

    public void initialize(JSONObject data) {
        if (data != null) {
            int value = ((Number) data.get(property)).intValue();
            indexField.setText(String.valueOf(value));
        }
    }

    @Override
    public void serializePart(JSONObject object) {
        object.put(property, Integer.parseInt(indexField.getText()));
    }

    @Override
    public void dispose() {

    }
}
