package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.data.GraphConnection;

public class GraphConnectionImpl implements GraphConnection {
    private String nodeFrom;
    private String fieldFrom;
    private String nodeTo;
    private String fieldTo;

    public GraphConnectionImpl(String nodeFrom, String fieldFrom, String nodeTo, String fieldTo) {
        this.nodeFrom = nodeFrom;
        this.fieldFrom = fieldFrom;
        this.nodeTo = nodeTo;
        this.fieldTo = fieldTo;
    }

    @Override
    public String getNodeFrom() {
        return nodeFrom;
    }

    public String getFieldFrom() {
        return fieldFrom;
    }

    public String getNodeTo() {
        return nodeTo;
    }

    public String getFieldTo() {
        return fieldTo;
    }
}
