package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.ui.WhitePixel;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBox;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyDefaultBox;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import org.json.simple.JSONObject;

public class PropertyColorBoxProducer implements PropertyBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Color");
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, JSONObject jsonObject) {
        String color = ((String) jsonObject.get("color"));
        return createPropertyBoxDefault(skin, name, color);
    }

    @Override
    public PropertyBox createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Color", "FFFFFFFF");
    }

    private PropertyBox createPropertyBoxDefault(Skin skin, String name, String colorStr) {
        Color color = Color.valueOf(colorStr);

        final TextureRegionDrawable drawable = new TextureRegionDrawable(WhitePixel.texture);
        BaseDrawable baseDrawable = new BaseDrawable(drawable) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                drawable.draw(batch, x, y, width, height);
            }
        };
        baseDrawable.setMinSize(20, 20);

        final Image image = new Image(baseDrawable);
        image.setColor(color);

        final ColorPicker picker = new ColorPicker(new ColorPickerAdapter() {
            @Override
            public void finished(Color newColor) {
                image.setColor(newColor);
            }
        });
        picker.setColor(color);

        image.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println(image.getWidth() + " " + image.getHeight());
                        //displaying picker with fade in animation
                        image.getStage().addActor(picker.fadeIn());
                    }
                });

        final Table table = new Table();
        table.add(new Label("Default: ", skin));
        table.add(new Label("Color", skin));
        table.add(image).grow();

        return new PropertyBoxImpl(skin, "Color",
                name,
                PropertyType.Color,
                new PropertyDefaultBox() {
                    @Override
                    public Actor getActor() {
                        return table;
                    }

                    @Override
                    public JSONObject serializeDefault() {
                        JSONObject result = new JSONObject();
                        result.put("color", image.getColor().toString());
                        return result;
                    }
                });
    }
}
