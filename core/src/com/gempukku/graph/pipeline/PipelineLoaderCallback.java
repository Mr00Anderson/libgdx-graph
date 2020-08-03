package com.gempukku.graph.pipeline;

import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import org.json.simple.JSONObject;

public interface PipelineLoaderCallback {
    void start();

    void addPipelineParticipant(GraphBoxProducer pipelineParticipantProducer, JSONObject object);

    void addPipelineParticipantConnection(String from, String to);

    void end();
}
