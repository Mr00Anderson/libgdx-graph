package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBox;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxProducer;
import org.json.simple.JSONObject;

public class PropertyModelsBoxProducer implements PropertyBoxProducer<PipelineFieldType> {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Models");
    }

    @Override
    public PropertyBox<PipelineFieldType> createPropertyBox(Skin skin, String name, JSONObject jsonObject) {
        return createPropertyBoxDefault(skin, name);
    }

    @Override
    public PropertyBox<PipelineFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Models");
    }

    private PropertyBox<PipelineFieldType> createPropertyBoxDefault(Skin skin, String name) {
        return new PropertyBoxImpl<PipelineFieldType>(skin, "Models",
                name,
                PipelineFieldType.Models, null);
    }
}
