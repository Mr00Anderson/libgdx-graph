package com.gempukku.libgdx.graph.pipeline;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class PipelineLoader {
    public static <T> T loadPipeline(InputStream inputStream, PipelineLoaderCallback<T> pipelineLoaderCallback) throws IOException {
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
                JSONObject data = (JSONObject) object.get("data");
                pipelineLoaderCallback.addPipelineNode(id, type, x, y, data);
            }
            for (JSONObject connection : (List<JSONObject>) pipeline.get("connections")) {
                String fromNode = (String) connection.get("fromNode");
                String fromField = (String) connection.get("fromField");
                String toNode = (String) connection.get("toNode");
                String toField = (String) connection.get("toField");
                pipelineLoaderCallback.addPipelineVertex(fromNode, fromField, toNode, toField);
            }
            for (JSONObject property : (List<JSONObject>) pipeline.get("properties")) {
                String type = (String) property.get("type");
                String name = (String) property.get("name");
                JSONObject data = (JSONObject) property.get("data");
                pipelineLoaderCallback.addPipelineProperty(type, name, data);
            }

            return pipelineLoaderCallback.end();
        } catch (ParseException exp) {
            throw new IOException("Unable to parse pipeline", exp);
        }
    }
}
