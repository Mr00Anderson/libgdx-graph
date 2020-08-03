package com.gempukku.graph.pipeline;

import com.gempukku.graph.pipeline.producer.PipelineParticipantProducer;
import org.json.simple.JSONObject;

public interface PipelineLoaderCallback {
    void start();

    void addPipelineParticipant(PipelineParticipantProducer pipelineParticipantProducer, JSONObject object);

    void addPipelineParticipantConnection(String from, String to);

    void end();
}
