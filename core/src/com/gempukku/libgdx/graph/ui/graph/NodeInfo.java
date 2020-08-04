package com.gempukku.libgdx.graph.ui.graph;

public class NodeInfo {
    private GraphBox graphBox;
    private GraphBoxInputConnector inputConnector;
    private GraphBoxOutputConnector outputConnector;

    public NodeInfo(GraphBox graphBox, GraphBoxInputConnector connector) {
        this.graphBox = graphBox;
        this.inputConnector = connector;
    }

    public NodeInfo(GraphBox graphBox, GraphBoxOutputConnector connector) {
        this.graphBox = graphBox;
        this.outputConnector = connector;
    }

    public GraphBox getGraphBox() {
        return graphBox;
    }

    public GraphBoxInputConnector getInputConnector() {
        return inputConnector;
    }

    public GraphBoxOutputConnector getOutputConnector() {
        return outputConnector;
    }

    public boolean isInput() {
        return inputConnector != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeInfo nodeInfo = (NodeInfo) o;

        if (!graphBox.equals(nodeInfo.graphBox)) return false;
        if (inputConnector != null ? !inputConnector.equals(nodeInfo.inputConnector) : nodeInfo.inputConnector != null)
            return false;
        return outputConnector != null ? outputConnector.equals(nodeInfo.outputConnector) : nodeInfo.outputConnector == null;
    }

    @Override
    public int hashCode() {
        int result = graphBox.hashCode();
        result = 31 * result + (inputConnector != null ? inputConnector.hashCode() : 0);
        result = 31 * result + (outputConnector != null ? outputConnector.hashCode() : 0);
        return result;
    }
}
