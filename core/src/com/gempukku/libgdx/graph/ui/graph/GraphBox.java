package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNode;
import org.json.simple.JSONObject;

public interface GraphBox<T extends FieldType> extends GraphNode<T> {
    Actor getActor();

    JSONObject serializeData();

    String getId();

    String getType();

    GraphBoxInputConnector<T> getInput(String fieldId);

    Iterable<GraphBoxInputConnector<T>> getInputs();

    GraphBoxOutputConnector<T> getOutput(String fieldId);

    Iterable<GraphBoxOutputConnector<T>> getOutputs();
}
