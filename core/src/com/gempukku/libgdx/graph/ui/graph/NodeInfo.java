package com.gempukku.libgdx.graph.ui.graph;

public class NodeInfo {
    private GraphBox graphBox;
    private GraphBoxConnector connector;

    public NodeInfo(GraphBox graphBox, GraphBoxConnector connector) {
        this.graphBox = graphBox;
        this.connector = connector;
    }

    public GraphBox getGraphBox() {
        return graphBox;
    }

    public GraphBoxConnector getConnector() {
        return connector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeInfo nodeInfo = (NodeInfo) o;

        if (!graphBox.equals(nodeInfo.graphBox)) return false;
        return connector.equals(nodeInfo.connector);
    }

    @Override
    public int hashCode() {
        int result = graphBox.hashCode();
        result = 31 * result + connector.hashCode();
        return result;
    }
}
