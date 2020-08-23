package com.gempukku.libgdx.graph.pipeline;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class GraphLoader {
    public static <T> T loadGraph(InputStream inputStream, GraphLoaderCallback<T> graphLoaderCallback) throws IOException {
        try {
            JSONParser parser = new JSONParser();
            JSONObject graph = (JSONObject) parser.parse(new InputStreamReader(inputStream));
            return loadGraph(graph, graphLoaderCallback);
        } catch (ParseException exp) {
            throw new IOException("Unable to parse graph", exp);
        }
    }

    public static <T> T loadGraph(JSONObject graph, GraphLoaderCallback<T> graphLoaderCallback) {
        graphLoaderCallback.start();
        for (JSONObject object : (List<JSONObject>) graph.get("nodes")) {
            String type = (String) object.get("type");
            String id = (String) object.get("id");
            float x = ((Number) object.get("x")).floatValue();
            float y = ((Number) object.get("y")).floatValue();
            JSONObject data = (JSONObject) object.get("data");
            graphLoaderCallback.addPipelineNode(id, type, x, y, data);
        }
        for (JSONObject connection : (List<JSONObject>) graph.get("connections")) {
            String fromNode = (String) connection.get("fromNode");
            String fromField = (String) connection.get("fromField");
            String toNode = (String) connection.get("toNode");
            String toField = (String) connection.get("toField");
            graphLoaderCallback.addPipelineVertex(fromNode, fromField, toNode, toField);
        }
        for (JSONObject property : (List<JSONObject>) graph.get("properties")) {
            String type = (String) property.get("type");
            String name = (String) property.get("name");
            JSONObject data = (JSONObject) property.get("data");
            graphLoaderCallback.addPipelineProperty(type, name, data);
        }

        return graphLoaderCallback.end();
    }
}
