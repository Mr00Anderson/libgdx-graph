package com.gempukku.graph.pipeline;

import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import org.json.simple.JSONObject;

public interface PipelineLoaderCallback {
    void start();

    void addPipelineParticipant(GraphBoxProducer pipelineParticipantProducer, String id, float x, float y, JSONObject data);

    void addPipelineParticipantConnection(String from, String to);

    void end();
}
