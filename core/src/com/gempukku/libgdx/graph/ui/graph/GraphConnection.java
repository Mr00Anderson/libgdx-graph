package com.gempukku.libgdx.graph.ui.graph;

public class GraphConnection {
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
}
