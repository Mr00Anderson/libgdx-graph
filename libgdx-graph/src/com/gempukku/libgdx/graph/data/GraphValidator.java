package com.gempukku.libgdx.graph.data;

import java.util.HashSet;
import java.util.Set;

public class GraphValidator {

    private GraphValidator() {

    }

    public static <T extends GraphNode, U extends GraphConnection> ValidationResult<T, U> validateGraph(Graph<T, U> graph, String nodeEnd) {
        ValidationResult<T, U> result = new ValidationResult<>();

        T end = graph.getNodeById(nodeEnd);
        if (end == null)
            return result;

        boolean cyclic = isCyclic(result, graph, nodeEnd);
        if (!cyclic) {
            // Do other Validation
        }
        return result;
    }

    // This function is a variation of DFSUtil() in
    // https://www.geeksforgeeks.org/archives/18212
    private static <T extends GraphNode, U extends GraphConnection> boolean isCyclicUtil(ValidationResult<T, U> validationResult, Graph<T, U> graph, String nodeId, Set<String> visited,
                                                                                         Set<String> recStack) {
        // Mark the current node as visited and
        // part of recursion stack
        if (recStack.contains(nodeId)) {
            return true;
        }

        if (visited.contains(nodeId))
            return false;

        visited.add(nodeId);
        recStack.add(nodeId);

        Set<String> connectedNodes = new HashSet<>();
        for (U incomingConnection : graph.getIncomingConnections(nodeId)) {
            connectedNodes.add(incomingConnection.getNodeFrom());
        }

        for (String connectedNode : connectedNodes) {
            if (isCyclicUtil(validationResult, graph, connectedNode, visited, recStack)) {
                validationResult.addErrorNode(graph.getNodeById(connectedNode));
                return true;
            }
        }
        recStack.remove(nodeId);

        return false;
    }

    private static <T extends GraphNode, U extends GraphConnection> boolean isCyclic(ValidationResult<T, U> validationResult, Graph<T, U> graph, String start) {
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        // Call the recursive helper function to
        // detect cycle in different DFS trees
        if (isCyclicUtil(validationResult, graph, start, visited, recStack)) {
            return true;
        }

        for (String nodeId : graph.getAllGraphNodes()) {
            if (!visited.contains(nodeId)) {
                validationResult.addWarningNode(graph.getNodeById(nodeId));
            }
        }
        return false;
    }

    public static class ValidationResult<T extends GraphNode, U extends GraphConnection> {
        private Set<T> errorNodes = new HashSet<>();
        private Set<T> warningNodes = new HashSet<>();
        private Set<U> errorConnections = new HashSet<>();

        public void addErrorNode(T node) {
            errorNodes.add(node);
        }

        public void addWarningNode(T node) {
            warningNodes.add(node);
        }

        public void addErrorConnection(U connection) {
            errorConnections.add(connection);
        }

        public Set<T> getErrorNodes() {
            return errorNodes;
        }

        public Set<T> getWarningNodes() {
            return warningNodes;
        }

        public Set<U> getErrorConnections() {
            return errorConnections;
        }
    }
}
