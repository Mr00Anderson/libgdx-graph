package com.gempukku.libgdx.graph.ui.graph.part;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxConnectorImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import org.json.simple.JSONObject;

import java.util.Collections;

public class ColorOutputGraphBoxPart implements GraphBoxPart {
    private String id;
    private String field;
    private Actor actor;
    private final Image image;

    public ColorOutputGraphBoxPart(Skin skin, String id, String field) {
        this(skin, id, field, "FFFFFFFF");
    }

    public ColorOutputGraphBoxPart(Skin skin, String id, String field, Object value) {
        this.id = id;
        this.field = field;

        image = new Image();
        image.setColor(Color.valueOf((String) value));

        final ColorPicker picker = new ColorPicker(new ColorPickerAdapter() {
            @Override
            public void finished(Color newColor) {
                image.setColor(newColor);
            }
        });

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        image.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        //displaying picker with fade in animation
                        image.getStage().addActor(picker.fadeIn());
                    }
                });

        horizontalGroup.addActor(new Label("Color", skin));
        horizontalGroup.addActor(image);

        this.actor = horizontalGroup;
    }

    @Override
    public Actor getActor() {
        return actor;
    }

    @Override
    public Iterable<? extends GraphBoxConnector> getConnectors() {
        return Collections.singleton(
                new GraphBoxConnectorImpl(id, GraphBoxConnector.Side.Right, GraphBoxConnector.CommunicationType.Output, PropertyType.Color, null));
    }

    @Override
    public void serializePart(JSONObject object) {
        object.put(field, image.getColor().toString());
    }
}
