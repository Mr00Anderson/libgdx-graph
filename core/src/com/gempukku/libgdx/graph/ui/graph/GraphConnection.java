package com.gempukku.libgdx.graph.ui.graph;

public class GraphConnection {
    private String from;
    private String to;

    public GraphConnection(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
