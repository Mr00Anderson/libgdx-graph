package com.gempukku.graph.pipeline;

import com.gempukku.graph.pipeline.producer.EndPipelineParticipantProducer;
import com.gempukku.graph.pipeline.producer.ObjectRendererParticipantProducer;
import com.gempukku.graph.pipeline.producer.PipelineParticipantProducer;
import com.gempukku.graph.pipeline.producer.StartPipelineParticipantProducer;
import com.gempukku.graph.pipeline.producer.UIRendererParticipantProducer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class PipelineSerializer {
    public static final List<PipelineParticipantProducer> producers = new LinkedList<>();

    static {
        producers.add(new StartPipelineParticipantProducer());
        producers.add(new EndPipelineParticipantProducer());
        producers.add(new ObjectRendererParticipantProducer());
        producers.add(new UIRendererParticipantProducer());
    }

    public static void loadPipeline(InputStream inputStream, PipelineLoaderCallback pipelineLoaderCallback) throws IOException {
        try {
            JSONParser parser = new JSONParser();
            JSONObject renderer = (JSONObject) parser.parse(new InputStreamReader(inputStream));

            pipelineLoaderCallback.start();
            JSONObject pipeline = (JSONObject) renderer.get("pipeline");
            for (JSONObject object : (List<JSONObject>) pipeline.get("objects")) {
                String type = (String) object.get("type");
                PipelineParticipantProducer producer = findProducerByType(type);
                if (producer == null)
                    throw new IOException("Unable to find pipeline producer for type: " + type);
                pipelineLoaderCallback.addPipelineParticipant(producer, object);
            }
            for (JSONObject connection : (List<JSONObject>) pipeline.get("connections")) {
                String from = (String) connection.get("from");
                String to = (String) connection.get("to");
                pipelineLoaderCallback.addPipelineParticipantConnection(from, to);
            }
            pipelineLoaderCallback.end();
        } catch (ParseException exp) {
            throw new IOException("Unable to parse pipeline", exp);
        }
    }

    private static PipelineParticipantProducer findProducerByType(String type) {
        for (PipelineParticipantProducer producer : producers) {
            if (producer.supportsType(type))
                return producer;
        }
        return null;
    }
}
