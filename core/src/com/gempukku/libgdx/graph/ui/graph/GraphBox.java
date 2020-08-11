package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.graph.data.GraphNode;
import org.json.simple.JSONObject;

public interface GraphBox extends GraphNode {
    Actor getActor();

    JSONObject serializeData();

    String getId();

    String getType();

    GraphBoxInputConnector getInput(String fieldId);

    Iterable<GraphBoxInputConnector> getGraphBoxInputConnectors();

    GraphBoxOutputConnector getOutput(String fieldId);

    Iterable<GraphBoxOutputConnector> getGraphBoxOutputConnectors();
}
