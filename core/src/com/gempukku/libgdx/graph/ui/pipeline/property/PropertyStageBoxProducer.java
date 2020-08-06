package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBox;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxImpl;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxProducer;
import org.json.simple.JSONObject;

public class PropertyStageBoxProducer implements PropertyBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Stage");
    }

    @Override
    public PropertyBox createPropertyBox(Skin skin, String name, JSONObject jsonObject) {
        return createPropertyBoxDefault(skin, name);
    }

    @Override
    public PropertyBox createDefaultPropertyBox(Skin skin) {
        return createPropertyBoxDefault(skin, "New Stage");
    }

    private PropertyBox createPropertyBoxDefault(Skin skin, String name) {
        return new PropertyBoxImpl(skin, "Stage",
                name,
                PropertyType.Stage, null);
    }
}
