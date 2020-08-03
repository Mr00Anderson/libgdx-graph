package com.gempukku.libgdx.graph.ui.graph.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxConnectorImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import org.json.simple.JSONObject;

import java.util.Collections;

public class ColorInputGraphBoxPart implements GraphBoxPart {
    private String id;
    private Actor actor;

    public ColorInputGraphBoxPart(Skin skin, String id) {
        this.id = id;

        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new Label("Background color", skin));

        actor = horizontalGroup;
    }

    @Override
    public Actor getActor() {
        return actor;
    }

    @Override
    public Iterable<? extends GraphBoxConnector> getConnectors() {
        return Collections.singleton(
                new GraphBoxConnectorImpl(id, GraphBoxConnector.Side.Left, GraphBoxConnector.CommunicationType.Input, PropertyType.Color, null));
    }

    @Override
    public void serializePart(JSONObject object) {

    }
}
