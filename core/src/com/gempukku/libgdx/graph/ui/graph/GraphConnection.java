package com.gempukku.libgdx.graph.ui.graph;

public class GraphConnection implements com.gempukku.libgdx.graph.data.GraphConnection {
    private NodeInfo from;
    private NodeInfo to;

    public GraphConnection(NodeInfo from, NodeInfo to) {
        this.from = from;
        this.to = to;
    }

    public NodeInfo getFrom() {
        return from;
    }

    public NodeInfo getTo() {
        return to;
    }

    @Override
    public String getNodeFrom() {
        return from.getGraphBox().getId();
    }
}
