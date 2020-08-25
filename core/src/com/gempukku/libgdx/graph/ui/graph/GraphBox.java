package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;

import java.util.Map;

public interface GraphBox<T extends FieldType> extends GraphNode<T> {
    Actor getActor();

    Map<String, GraphBoxInputConnector<T>> getInputs();

    Map<String, GraphBoxOutputConnector<T>> getOutputs();

    void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph<? extends GraphNode<T>, ? extends GraphConnection, ? extends GraphProperty<T>, T> graph);
}
