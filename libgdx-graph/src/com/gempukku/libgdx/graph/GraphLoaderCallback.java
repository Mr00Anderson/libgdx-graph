package com.gempukku.libgdx.graph;

import org.json.simple.JSONObject;

public interface GraphLoaderCallback<T> {
    void start();

    void addPipelineNode(String id, String type, float x, float y, JSONObject data);

    void addPipelineVertex(String fromNode, String fromField, String toNode, String toField);

    void addPipelineProperty(String type, String name, JSONObject data);

    T end();
}
