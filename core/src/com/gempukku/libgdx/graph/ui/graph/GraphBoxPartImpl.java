package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.graph.pipeline.PropertyType;
import com.google.common.base.Predicate;
import org.json.simple.JSONObject;

public class GraphBoxPartImpl implements GraphBoxPart {
    private Actor actor;
    private GraphBoxInputConnector inputConnector;
    private GraphBoxOutputConnector outputConnector;
    private Callback callback;

    public GraphBoxPartImpl(Actor actor, Callback callback) {
        this.actor = actor;
        this.callback = callback;
    }

    public void setInputConnector(String id, GraphBoxInputConnector.Side side, Predicate<PropertyType> propertyPredicate) {
        inputConnector = new GraphBoxInputConnectorImpl(id, side, propertyPredicate, null);
    }

    public void setOutputConnector(String id, GraphBoxOutputConnector.Side side, PropertyType propertyType) {
        outputConnector = new GraphBoxOutputConnectorImpl(id, side, propertyType, null);
    }

    @Override
    public Actor getActor() {
        return actor;
    }

    @Override
    public GraphBoxInputConnector getInputConnector() {
        return inputConnector;
    }

    @Override
    public GraphBoxOutputConnector getOutputConnector() {
        return outputConnector;
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
