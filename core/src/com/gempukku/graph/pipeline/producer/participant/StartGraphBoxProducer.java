package com.gempukku.graph.pipeline.producer.participant;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.google.common.base.Predicates;
import org.json.simple.JSONObject;

public class StartGraphBoxProducer implements GraphBoxProducer {
    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Start";
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("PipelineStart");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JSONObject jsonObject) {
        GraphBoxImpl start = new GraphBoxImpl(id, "PipelineStart", skin);
        start.addBottomConnector(id + ":output");
        start.addInputGraphPart(skin,
                id + ":background", "Background color", Predicates.equalTo(PropertyType.Color));
        start.addInputGraphPart(skin,
                id + ":size", "Size", Predicates.equalTo(PropertyType.Vector2));

        return start;
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        return null;
    }
}
