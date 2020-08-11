package com.gempukku.libgdx.graph.ui.producer.value;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import org.json.simple.JSONObject;

public class ValueBooleanBoxProducer extends ValueGraphBoxProducer {
    public ValueBooleanBoxProducer(PipelineNodeConfiguration configuration) {
        super(configuration);
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JSONObject data) {
        boolean v = (Boolean) data.get("v");

        return createGraphBox(skin, id, v);
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        return createGraphBox(skin, id, false);
    }

    private GraphBox createGraphBox(Skin skin, String id, boolean v) {
        GraphBoxImpl end = new GraphBoxImpl(id, configuration, skin);
        end.addGraphBoxPart(createValuePart(skin, v));

        return end;
    }

    private GraphBoxPartImpl createValuePart(Skin skin, boolean v) {
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        final CheckBox checkBox = new CheckBox("Value", skin);
        checkBox.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        checkBox.fire(new GraphChangedEvent(false));
                    }
                });
        checkBox.setChecked(v);
        horizontalGroup.addActor(checkBox);

        GraphBoxPartImpl colorPart = new GraphBoxPartImpl(horizontalGroup,
                new GraphBoxPartImpl.Callback() {
                    @Override
                    public void serialize(JSONObject object) {
                        object.put("v", checkBox.isChecked());
                    }
                });
        colorPart.setOutputConnector(GraphBoxOutputConnector.Side.Right, configuration.getNodeOutput("value"));
        return colorPart;
    }
}
