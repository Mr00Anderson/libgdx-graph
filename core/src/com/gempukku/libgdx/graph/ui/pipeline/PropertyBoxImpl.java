package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import org.json.simple.JSONObject;

public class PropertyBoxImpl extends Table implements PropertyBox {
    private String id;
    private String type;
    private PropertyDefaultBox propertyDefaultBox;
    private TextField textField;

    public PropertyBoxImpl(Skin skin, String id, String type, PropertyDefaultBox propertyDefaultBox) {
        super(skin);
        this.id = id;
        this.type = type;
        this.propertyDefaultBox = propertyDefaultBox;

        textField = new TextField("", skin);
        Table headerTable = new Table(skin);
        headerTable.add(new Label("Name:", skin));
        headerTable.add(textField).growX();
        headerTable.row();
        add(headerTable).growX().row();
        add(propertyDefaultBox.getActor()).growX().row();
    }

    @Override
    public String getPropertyName() {
        return textField.getName();
    }

    @Override
    public JSONObject serializeProperty() {
        JSONObject result = new JSONObject();
        result.put("id", id);
        result.put("type", type);
        propertyDefaultBox.serializeDefault(result);
        return result;
    }

    @Override
    public Actor getActor() {
        return this;
    }
}
