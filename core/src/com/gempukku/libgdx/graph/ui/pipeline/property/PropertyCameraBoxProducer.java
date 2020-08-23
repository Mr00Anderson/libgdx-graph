package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import org.json.simple.JSONObject;

public class PropertyCameraBoxProducer implements PropertyBoxProducer<PipelineFieldType> {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Camera");
    }

    @Override
    public PropertyBox<PipelineFieldType> createPropertyBox(Skin skin, String name, JSONObject jsonObject) {
        return createPropertyBoxDefault(skin, name);
    }

    @Override
    public PropertyBox<PipelineFieldType> createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Camera");
    }

    private PropertyBox<PipelineFieldType> createPropertyBoxDefault(Skin skin, String name) {
        return new PropertyBoxImpl<PipelineFieldType>(skin, "Camera",
                name, PipelineFieldType.Camera, null);
    }
}
