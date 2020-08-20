package com.gempukku.libgdx.graph.data;

public interface Graph<T extends GraphNode<V>, U extends GraphConnection, V extends FieldType> {
    T getNodeById(String id);

    Iterable<? extends U> getIncomingConnections(String nodeId);

    Iterable<String> getAllGraphNodes();
//    List<String> getPropertyNames();
//    PropertyType getPropertyType(String propertyName);
}
