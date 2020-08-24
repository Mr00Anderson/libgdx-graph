package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNode;
import org.json.simple.JSONObject;

import java.util.Map;

public interface GraphBox<T extends FieldType> extends GraphNode<T> {
    Actor getActor();

    JSONObject serializeData();

    String getId();

    String getType();

    Map<String, GraphBoxInputConnector<T>> getInputs();

    Map<String, GraphBoxOutputConnector<T>> getOutputs();
}
