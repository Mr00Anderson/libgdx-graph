package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.ValueGraphNodeOutput;
import org.json.simple.JSONObject;

public class PropertyPipelineGraphBoxProducer implements GraphBoxProducer<PipelineFieldType> {
    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getName() {
        return "Property";
    }

    @Override
    public String getMenuLocation() {
        return null;
    }

    @Override
    public GraphBox<PipelineFieldType> createPipelineGraphBox(Skin skin, String id, JSONObject data) {
        final String name = (String) data.get("name");
        final PipelineFieldType propertyType = PipelineFieldType.valueOf((String) data.get("type"));
        GraphBoxImpl<PipelineFieldType> result = new GraphBoxImpl<PipelineFieldType>(id, new PropertyNodeConfiguration<>(name, propertyType), skin) {
            @Override
            public JSONObject getData() {
                JSONObject result = new JSONObject();
                result.put("name", name);
                result.put("type", propertyType.name());
                return result;
            }
        };
        result.addOutputGraphPart(skin, new ValueGraphNodeOutput<>(name, propertyType));

        return result;
    }

    @Override
    public GraphBox<PipelineFieldType> createDefault(Skin skin, String id) {
        return null;
    }
}
