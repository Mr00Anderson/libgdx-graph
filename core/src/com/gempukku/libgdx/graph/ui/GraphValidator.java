package com.gempukku.libgdx.graph.ui;

import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphConnection;
import com.gempukku.libgdx.graph.ui.graph.GraphContainer;
import com.google.common.collect.Iterables;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GraphValidator {
    public enum ValidationResult {
        Valid, Acceptable, Invalid;
    }

    private GraphValidator() {

    }

    public static ValidationResult validateGraph(GraphContainer graphContainer, GraphBox graphEnd) {
        return isCyclic(graphContainer, graphEnd, Iterables.size(graphContainer.getGraphBoxes()));
    }

    // This function is a variation of DFSUtil() in
    // https://www.geeksforgeeks.org/archives/18212
    private static boolean isCyclicUtil(GraphContainer graphContainer, GraphBox node, Set<String> visited,
                                        Set<String> recStack) {

        // Mark the current node as visited and
        // part of recursion stack
        String nodeId = node.getId();
        if (recStack.contains(nodeId))
            return true;

        if (visited.contains(nodeId))
            return false;

        visited.add(nodeId);
        recStack.add(nodeId);

        List<GraphBox> connectedNodes = new LinkedList<>();
        for (GraphConnection incomingConnection : graphContainer.getIncomingConnections(node)) {
            connectedNodes.add(incomingConnection.getFrom().getGraphBox());
        }

        for (GraphBox connectedNode : connectedNodes) {
            if (isCyclicUtil(graphContainer, connectedNode, visited, recStack))
                return true;
        }
        recStack.remove(nodeId);

        return false;
    }

    private static ValidationResult isCyclic(GraphContainer graphContainer, GraphBox start, int expectedNodeCount) {
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        // Call the recursive helper function to
        // detect cycle in different DFS trees
        if (isCyclicUtil(graphContainer, start, visited, recStack))
            return ValidationResult.Invalid;

        if (visited.size() == expectedNodeCount)
            return ValidationResult.Valid;
        else
            return ValidationResult.Acceptable;
    }

}
