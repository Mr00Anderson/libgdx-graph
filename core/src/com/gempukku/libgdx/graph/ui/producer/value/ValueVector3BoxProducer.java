package com.gempukku.libgdx.graph.ui.producer.value;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.graph.NodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import org.json.simple.JSONObject;

public class ValueVector3BoxProducer<T extends FieldType> extends ValueGraphBoxProducer<T> {
    public ValueVector3BoxProducer(NodeConfiguration<T> configuration) {
        super(configuration);
    }

    @Override
    public GraphBox<T> createPipelineGraphBox(Skin skin, String id, JSONObject data) {
        float v1 = ((Number) data.get("v1")).floatValue();
        float v2 = ((Number) data.get("v2")).floatValue();
        float v3 = ((Number) data.get("v3")).floatValue();

        return createGraphBox(skin, id, v1, v2, v3);
    }

    @Override
    public GraphBox<T> createDefault(Skin skin, String id) {
        return createGraphBox(skin, id, 0, 0, 0);
    }

    private GraphBox<T> createGraphBox(Skin skin, String id, float v1, float v2, float v3) {
        GraphBoxImpl<T> end = new GraphBoxImpl<T>(id, configuration, skin);
        end.addGraphBoxPart(createValuePart(skin, v1, v2, v3));

        return end;
    }

    private GraphBoxPartImpl<T> createValuePart(Skin skin, float v1, float v2, float v3) {
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
        final VisValidatableTextField v2Input = new VisValidatableTextField(new Validators.FloatValidator()) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v2Input.setText(String.valueOf(v2));
        v2Input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        v2Input.fire(new GraphChangedEvent(false));
                    }
                });
        final VisValidatableTextField v3Input = new VisValidatableTextField(new Validators.FloatValidator()) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        v3Input.setText(String.valueOf(v3));
        v3Input.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        v3Input.fire(new GraphChangedEvent(false));
                    }
                });

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new Label("x", skin));
        horizontalGroup.addActor(v1Input);
        horizontalGroup.addActor(new Label("y", skin));
        horizontalGroup.addActor(v2Input);
        horizontalGroup.addActor(new Label("z", skin));
        horizontalGroup.addActor(v3Input);

        GraphBoxPartImpl<T> colorPart = new GraphBoxPartImpl<T>(horizontalGroup,
                new GraphBoxPartImpl.Callback() {
                    @Override
                    public void serialize(JSONObject object) {
                        object.put("v1", Float.parseFloat(v1Input.getText()));
                        object.put("v2", Float.parseFloat(v2Input.getText()));
                        object.put("v3", Float.parseFloat(v3Input.getText()));
                    }
                });
        colorPart.setOutputConnector(GraphBoxOutputConnector.Side.Right, configuration.getNodeOutput("value"));
        return colorPart;
    }
}
