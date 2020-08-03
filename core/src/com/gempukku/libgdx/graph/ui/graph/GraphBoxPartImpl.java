package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.graph.pipeline.PropertyType;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class GraphBoxPartImpl implements GraphBoxPart {
    private Actor actor;
    private List<GraphBoxConnector> connectors = new LinkedList<>();
    private Callback callback;

    public GraphBoxPartImpl(Actor actor, Callback callback) {
        this.actor = actor;
        this.callback = callback;
    }

    public void addConnector(String id, GraphBoxConnector.Side side, GraphBoxConnector.CommunicationType communicationType, PropertyType propertyType) {
        connectors.add(new GraphBoxConnectorImpl(id, side, communicationType, propertyType, null));
    }

    @Override
    public Actor getActor() {
        return actor;
    }

    @Override
    public Iterable<? extends GraphBoxConnector> getConnectors() {
        return connectors;
    }

    @Override
    public void serializePart(JSONObject object) {
        if (callback != null)
            callback.serialize(object);
    }

    public interface Callback {
        void serialize(JSONObject object);
    }
}
