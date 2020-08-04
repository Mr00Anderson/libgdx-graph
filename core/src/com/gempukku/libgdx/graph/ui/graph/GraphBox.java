package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.scenes.scene2d.ui.Window;
import org.json.simple.JSONObject;

public interface GraphBox {
    Window getWindow();

    JSONObject serializeGraphBox();

    Iterable<GraphBoxInputConnector> getGraphBoxInputConnectors();

    Iterable<GraphBoxOutputConnector> getGraphBoxOutputConnectors();
}
