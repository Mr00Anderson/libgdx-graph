package com.gempukku.libgdx.graph.pipeline;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class PipelineSerializer {
    public static void loadPipeline(InputStream inputStream, PipelineLoaderCallback pipelineLoaderCallback) throws IOException {
        try {
            JSONParser parser = new JSONParser();
            JSONObject renderer = (JSONObject) parser.parse(new InputStreamReader(inputStream));

            pipelineLoaderCallback.start();
            JSONObject pipeline = (JSONObject) renderer.get("pipeline");
            for (JSONObject object : (List<JSONObject>) pipeline.get("objects")) {
                String type = (String) object.get("type");
                String id = (String) object.get("id");
                float x = ((Number) object.get("x")).floatValue();
                float y = ((Number) object.get("y")).floatValue();
                pipelineLoaderCallback.addPipelineParticipant(id, type, x, y, (JSONObject) object.get("data"));
            }
            for (JSONObject connection : (List<JSONObject>) pipeline.get("connections")) {
                String from = (String) connection.get("from");
                String to = (String) connection.get("to");
                pipelineLoaderCallback.addPipelineParticipantConnection(from, to);
            }
            for (JSONObject property : (List<JSONObject>) pipeline.get("properties")) {
                String type = (String) property.get("type");
                JSONObject data = (JSONObject) property.get("data");
                pipelineLoaderCallback.addPipelineProperty(type, data);
            }

            pipelineLoaderCallback.end();
        } catch (ParseException exp) {
            throw new IOException("Unable to parse pipeline", exp);
        }
    }
}
