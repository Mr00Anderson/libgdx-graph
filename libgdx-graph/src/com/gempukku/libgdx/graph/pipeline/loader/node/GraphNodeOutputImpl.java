package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;

import java.util.Arrays;
import java.util.List;

public class GraphNodeOutputImpl<T extends FieldType> implements GraphNodeOutput<T> {
    private String id;
    private String name;
    private boolean mainConnection;
    private List<? extends T> propertyTypes;

    public GraphNodeOutputImpl(String id, String name, T... producedType) {
        this(id, name, false, producedType);
    }

    public GraphNodeOutputImpl(String id, String name, boolean mainConnection, T... producedType) {
        this.id = id;
        this.name = name;
        this.mainConnection = mainConnection;
        this.propertyTypes = Arrays.asList(producedType);
    }

    @Override
    public String getFieldId() {
        return id;
    }

    @Override
    public boolean isMainConnection() {
        return mainConnection;
    }

    @Override
    public String getFieldName() {
        return name;
    }

    @Override
    public List<? extends T> getProducablePropertyTypes() {
        return propertyTypes;
    }

    @Override
    public boolean supportsMultiple() {
        return !mainConnection;
    }
}
