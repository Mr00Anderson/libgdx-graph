package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.renderer.config.PropertyPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.producer.ValueGraphNodeOutput;
import org.json.simple.JSONObject;

public class PropertyBoxImpl<T extends FieldType> extends Table implements PropertyBox<T> {
    private String type;
    private T propertyType;
    private PropertyDefaultBox propertyDefaultBox;
    private TextField textField;

    public PropertyBoxImpl(Skin skin, String type, String name, T propertyType,
                           PropertyDefaultBox propertyDefaultBox) {
        super(skin);
        this.type = type;
        this.propertyType = propertyType;
        this.propertyDefaultBox = propertyDefaultBox;

        textField = new TextField(name, skin);
        Table headerTable = new Table(skin);
        headerTable.add(new Label("Name: ", skin));
        headerTable.add(textField).growX();
        textField.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        fire(new GraphChangedEvent(true));
                    }
                });
        headerTable.row();
        add(headerTable).growX().row();
        if (propertyDefaultBox != null)
            add(propertyDefaultBox.getActor()).growX().row();
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return textField.getText();
    }

    @Override
    public JSONObject serializeData() {
        if (propertyDefaultBox != null) {
            JSONObject data = propertyDefaultBox.serializeData();
            if (data == null)
                return null;
            return data;
        } else {
            return null;
        }
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphBox<T> createPropertyBox(Skin skin, String id, float x, float y) {
        final String name = getName();
        GraphBoxImpl<T> result = new GraphBoxImpl<T>(id, new PropertyPipelineNodeConfiguration("Property", name, propertyType), skin) {
            @Override
            public JSONObject serializeData() {
                JSONObject result = new JSONObject();
                result.put("name", name);
                result.put("type", propertyType.name());
                return result;
            }
        };
        result.addOutputGraphPart(skin, new ValueGraphNodeOutput(name, propertyType));
        return result;
    }
}
