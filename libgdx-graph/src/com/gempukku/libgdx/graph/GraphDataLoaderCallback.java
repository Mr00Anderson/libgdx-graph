package com.gempukku.libgdx.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.graph.data.GraphProperty;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class GraphDataLoaderCallback<T, U extends FieldType> implements GraphLoaderCallback<T>, Graph<GraphNode<U>, GraphConnection, GraphProperty<U>, U> {
    private Map<String, GraphNodeData> graphNodes = new HashMap<>();
    private List<GraphConnectionData> graphConnections = new LinkedList<>();
    private Map<String, GraphPropertyData> graphProperties = new HashMap<>();

    @Override
    public void addPipelineNode(String id, String type, float x, float y, JSONObject data) {
        graphNodes.put(id, new GraphNodeData(id, getNodeConfiguration(type, data), data));
    }

    @Override
    public void addPipelineVertex(String fromNode, String fromField, String toNode, String toField) {
        graphConnections.add(new GraphConnectionData(fromNode, fromField, toNode, toField));
    }

    @Override
    public void addPipelineProperty(String type, String name, JSONObject data) {
        graphProperties.put(name, new GraphPropertyData(name, getFieldType(type), data));
    }

    @Override
    public GraphNode<U> getNodeById(String id) {
        return graphNodes.get(id);
    }

    @Override
    public GraphProperty<U> getPropertyByName(String name) {
        return graphProperties.get(name);
    }

    @Override
    public Iterable<? extends GraphConnection> getConnections() {
        return graphConnections;
    }

    @Override
    public Iterable<? extends GraphNode<U>> getNodes() {
        return graphNodes.values();
    }

    @Override
    public Iterable<? extends GraphProperty<U>> getProperties() {
        return graphProperties.values();
    }

    protected abstract U getFieldType(String type);

    protected abstract NodeConfiguration<U> getNodeConfiguration(String type, JSONObject data);

    protected class GraphNodeData implements GraphNode<U> {
        private String id;
        private NodeConfiguration<U> configuration;
        private JSONObject data;

        public GraphNodeData(String id, NodeConfiguration<U> configuration, JSONObject data) {
            this.id = id;
            this.configuration = configuration;
            this.data = data;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public boolean isInputField(String fieldId) {
            return configuration.getNodeInputs().containsKey(fieldId);
        }

        @Override
        public Map<String, ? extends GraphNodeInput<U>> getInputs() {
            return configuration.getNodeInputs();
        }

        @Override
        public Map<String, ? extends GraphNodeOutput<U>> getOutputs() {
            return configuration.getNodeOutputs();
        }

        public JSONObject getData() {
            return data;
        }
    }

    protected class GraphPropertyData implements GraphProperty<U> {
        private String name;
        private U type;
        private JSONObject data;

        public GraphPropertyData(String name, U type, JSONObject data) {
            this.name = name;
            this.type = type;
            this.data = data;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public U getType() {
            return type;
        }

        public JSONObject getData() {
            return data;
        }
    }

    protected class GraphConnectionData implements GraphConnection {
        private String nodeFrom;
        private String fieldFrom;
        private String nodeTo;
        private String fieldTo;

        public GraphConnectionData(String nodeFrom, String fieldFrom, String nodeTo, String fieldTo) {
            this.nodeFrom = nodeFrom;
            this.fieldFrom = fieldFrom;
            this.nodeTo = nodeTo;
            this.fieldTo = fieldTo;
        }

        @Override
        public String getNodeFrom() {
            return nodeFrom;
        }

        @Override
        public String getFieldFrom() {
            return fieldFrom;
        }

        @Override
        public String getNodeTo() {
            return nodeTo;
        }

        @Override
        public String getFieldTo() {
            return fieldTo;
        }
    }
}
