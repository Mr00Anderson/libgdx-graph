package com.gempukku.libgdx.graph.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphValidator {

    private GraphValidator() {

    }

    public static <T extends GraphNode<V>, U extends GraphConnection, V extends FieldType> ValidationResult<T, U, V> validateGraph(Graph<T, U, V> graph, String nodeEnd) {
        ValidationResult<T, U, V> result = new ValidationResult<>();

        T end = graph.getNodeById(nodeEnd);
        if (end == null)
            return result;

        boolean cyclic = isCyclic(result, graph, nodeEnd);
        if (!cyclic) {
            // Do other Validation
            validateNode(result, graph, nodeEnd);
        }
        return result;
    }

    private static <T extends GraphNode<V>, U extends GraphConnection, V extends FieldType> void validateNode(ValidationResult<T, U, V> result, Graph<T, U, V> graph, String nodeId) {
        T thisNode = graph.getNodeById(nodeId);
        Set<String> validatedFields = new HashSet<>();
        for (U incomingConnection : graph.getIncomingConnections(nodeId)) {
            String fieldTo = incomingConnection.getFieldTo();
            GraphNodeInput<V> input = thisNode.getInput(fieldTo);
            T remoteNode = graph.getNodeById(incomingConnection.getNodeFrom());
            GraphNodeOutput<V> output = remoteNode.getOutput(incomingConnection.getFieldFrom());

            // Validate the actual output is accepted by the input
            List<? extends V> acceptedPropertyTypes = input.getAcceptedPropertyTypes();
            if (!outputAcceptsPropertyType(output, acceptedPropertyTypes)) {
                result.addErrorConnection(incomingConnection);
            }

            validatedFields.add(fieldTo);
            validateNode(result, graph, incomingConnection.getNodeFrom());
        }

        for (GraphNodeInput<V> input : thisNode.getInputs()) {
            if (input.isRequired() && !validatedFields.contains(input.getFieldId()))
                result.addErrorConnector(new NodeConnector(nodeId, input.getFieldId()));
        }
    }

    private static boolean outputAcceptsPropertyType(GraphNodeOutput<? extends FieldType> output, List<? extends FieldType> acceptedPropertyTypes) {
        List<? extends FieldType> producablePropertyTypes = output.getProducablePropertyTypes();
        for (FieldType acceptedFieldType : acceptedPropertyTypes) {
            if (producablePropertyTypes.contains(acceptedFieldType))
                return true;
        }
        return false;
    }

    // This function is a variation of DFSUtil() in
    // https://www.geeksforgeeks.org/archives/18212
    private static <T extends GraphNode<V>, U extends GraphConnection, V extends FieldType> boolean isCyclicUtil(ValidationResult<T, U, V> validationResult, Graph<T, U, V> graph, String nodeId, Set<String> visited,
                                                                                                                 Set<String> recStack) {
        // Mark the current node as visited and
        // part of recursion stack
        if (recStack.contains(nodeId)) {
            validationResult.addErrorNode(graph.getNodeById(nodeId));
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
                return true;
            }
        }
        recStack.remove(nodeId);

        return false;
    }

    private static <T extends GraphNode<V>, U extends GraphConnection, V extends FieldType> boolean isCyclic(ValidationResult<T, U, V> validationResult, Graph<T, U, V> graph, String start) {
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

    public static class ValidationResult<T extends GraphNode<V>, U extends GraphConnection, V extends FieldType> {
        private final Set<T> errorNodes = new HashSet<>();
        private final Set<T> warningNodes = new HashSet<>();
        private final Set<U> errorConnections = new HashSet<>();
        private final Set<NodeConnector> errorConnectors = new HashSet<>();

        public void addErrorNode(T node) {
            errorNodes.add(node);
        }

        public void addWarningNode(T node) {
            warningNodes.add(node);
        }

        public void addErrorConnection(U connection) {
            errorConnections.add(connection);
        }

        public void addErrorConnector(NodeConnector nodeConnector) {
            errorConnectors.add(nodeConnector);
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

        public Set<NodeConnector> getErrorConnectors() {
            return errorConnectors;
        }

        public boolean hasErrors() {
            return !errorNodes.isEmpty() || !errorConnections.isEmpty() || !errorConnectors.isEmpty();
        }

        public boolean hasWarnings() {
            return !warningNodes.isEmpty();
        }
    }
}
