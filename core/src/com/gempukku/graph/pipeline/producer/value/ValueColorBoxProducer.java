package com.gempukku.graph.pipeline.producer.value;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import org.json.simple.JSONObject;

public class ValueColorBoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("ValueColor");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();
        String value = (String) jsonObject.get("color");

        return createGraphBox(skin, id, x, y, value);
    }

    @Override
    public GraphBox createDefault(Skin skin, String id, float x, float y) {
        return createGraphBox(skin, id, x, y, "FFFFFFFF");
    }

    private GraphBox createGraphBox(Skin skin, String id, float x, float y, String value) {
        GraphBoxImpl end = new GraphBoxImpl(id, "ValueColor", "Color", skin);
        end.setPosition(x, y);
        end.addGraphBoxPart(createValuePart(skin, id, value));

        return end;
    }

    private GraphBoxPartImpl createValuePart(Skin skin, String id, String value) {
        final Image image = new Image();
        image.setColor(Color.valueOf(value));

        final ColorPicker picker = new ColorPicker(new ColorPickerAdapter() {
            @Override
            public void finished(Color newColor) {
                image.setColor(newColor);
            }
        });

        image.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        //displaying picker with fade in animation
                        image.getStage().addActor(picker.fadeIn());
                    }
                });


        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new Label("Color", skin));
        horizontalGroup.addActor(image);

        GraphBoxPartImpl colorPart = new GraphBoxPartImpl(horizontalGroup,
                new GraphBoxPartImpl.Callback() {
                    @Override
                    public void serialize(JSONObject object) {
                        object.put("color", image.getColor().toString());
                    }
                });
        colorPart.setOutputConnector(id, GraphBoxOutputConnector.Side.Right, PropertyType.Color);
        return colorPart;
    }
}
