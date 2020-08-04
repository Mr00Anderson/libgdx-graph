package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import org.json.simple.JSONObject;

public class PropertyBoxImpl extends Table implements PropertyBox {
    private String id;
    private String type;
    private PropertyType propertyType;
    private PropertyDefaultBox propertyDefaultBox;
    private TextField textField;

    public PropertyBoxImpl(Skin skin, String id, String type, PropertyType propertyType,
                           PropertyDefaultBox propertyDefaultBox) {
        super(skin);
        this.id = id;
        this.type = type;
        this.propertyType = propertyType;
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
    public String getName() {
        return textField.getText();
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

    @Override
    public GraphBox createPropertyBox(Skin skin, String id, float x, float y) {
        final String name = getName();
        GraphBoxImpl result = new GraphBoxImpl(id, "Property", skin) {
            @Override
            public JSONObject serializeData() {
                JSONObject result = new JSONObject();
                result.put("Name", name);
                return result;
            }
        };
        result.addOutputGraphPart(skin, id + ":value", name, propertyType);
        return result;
    }
}
