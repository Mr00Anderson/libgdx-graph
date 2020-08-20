package com.gempukku.libgdx.graph.ui.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.config.PropertyPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import org.json.simple.JSONObject;

public class PropertyGraphBoxProducer implements GraphBoxProducer<PipelineFieldType> {
    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Property";
    }

    @Override
    public GraphBox<PipelineFieldType> createPipelineGraphBox(Skin skin, String id, JSONObject data) {
        final String name = (String) data.get("name");
        final PipelineFieldType propertyType = PipelineFieldType.valueOf((String) data.get("type"));
        GraphBoxImpl<PipelineFieldType> result = new GraphBoxImpl<PipelineFieldType>(id, new PropertyPipelineNodeConfiguration("Property", name, propertyType), skin) {
            @Override
            public JSONObject serializeData() {
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
    public GraphBox createDefault(Skin skin, String id) {
        return null;
    }
}
