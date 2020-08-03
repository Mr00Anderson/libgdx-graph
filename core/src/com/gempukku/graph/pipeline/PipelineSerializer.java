package com.gempukku.graph.pipeline;

import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.graph.pipeline.producer.participant.EndGraphBoxProducer;
import com.gempukku.graph.pipeline.producer.participant.ObjectRendererBoxProducer;
import com.gempukku.graph.pipeline.producer.participant.StartGraphBoxProducer;
import com.gempukku.graph.pipeline.producer.participant.UIRendererBoxProducer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class PipelineSerializer {
    public static final List<GraphBoxProducer> producers = new LinkedList<>();

    static {
        producers.add(new StartGraphBoxProducer());
        producers.add(new EndGraphBoxProducer());
        producers.add(new ObjectRendererBoxProducer());
        producers.add(new UIRendererBoxProducer());
    }

    public static void loadPipeline(InputStream inputStream, PipelineLoaderCallback pipelineLoaderCallback) throws IOException {
        try {
            JSONParser parser = new JSONParser();
            JSONObject renderer = (JSONObject) parser.parse(new InputStreamReader(inputStream));

            pipelineLoaderCallback.start();
            JSONObject pipeline = (JSONObject) renderer.get("pipeline");
            for (JSONObject object : (List<JSONObject>) pipeline.get("objects")) {
                String type = (String) object.get("type");
                GraphBoxProducer producer = findProducerByType(type);
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

    private static GraphBoxProducer findProducerByType(String type) {
        for (GraphBoxProducer producer : producers) {
            if (producer.supportsType(type))
                return producer;
        }
        return null;
    }
}
