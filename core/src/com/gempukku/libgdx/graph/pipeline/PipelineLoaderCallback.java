package com.gempukku.libgdx.graph.pipeline;

import org.json.simple.JSONObject;

public interface PipelineLoaderCallback {
    void start();

    void addPipelineParticipant(String id, String type, float x, float y, JSONObject data);

    void addPipelineParticipantConnection(String from, String to);

    void addPipelineProperty(String type, String name, JSONObject data);

    void end();
}
