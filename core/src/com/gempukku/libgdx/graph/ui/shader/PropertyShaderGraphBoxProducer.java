package com.gempukku.libgdx.graph.ui.shader;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.ValueGraphNodeOutput;
import org.json.simple.JSONObject;

public class PropertyShaderGraphBoxProducer implements GraphBoxProducer<ShaderFieldType> {
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
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JSONObject data) {
        final String name = (String) data.get("name");
        final ShaderFieldType propertyType = ShaderFieldType.valueOf((String) data.get("type"));
        GraphBoxImpl<ShaderFieldType> result = new GraphBoxImpl<ShaderFieldType>(id, new PropertyNodeConfiguration<>(name, propertyType), skin) {
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
    public GraphBox<ShaderFieldType> createDefault(Skin skin, String id) {
        return null;
    }
}
