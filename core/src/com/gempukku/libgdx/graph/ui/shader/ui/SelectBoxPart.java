package com.gempukku.libgdx.graph.ui.shader.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import org.json.simple.JSONObject;

public class SelectBoxPart<T extends FieldType> extends Table implements GraphBoxPart<T> {
    private final SelectBox<String> selectBox;

    private String property;

    public SelectBoxPart(Skin skin, String label, String property, Enum<?>... values) {
        this(skin, label, property, convertToStrings(values));
    }

    private static String[] convertToStrings(Enum<?>[] values) {
        String[] result = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i].name();
        }
        return result;
    }

    public SelectBoxPart(Skin skin, String label, String property, String... values) {
        super(skin);
        this.property = property;

        selectBox = new SelectBox<String>(skin);
        selectBox.setItems(values);
        add(new Label(label + " ", skin));
        add(selectBox).growX();
        row();

        selectBox.addListener(
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
            String value = (String) data.get(property);
            selectBox.setSelected(value);
        }
    }

    @Override
    public void serializePart(JSONObject object) {
        object.put(property, selectBox.getSelected());
    }

    @Override
    public void dispose() {

    }
}
