package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;

import java.util.Arrays;
import java.util.List;

public class PipelineNodeOutputImpl implements PipelineNodeOutput {
    private String id;
    private String name;
    private boolean mainConnection;
    private List<PropertyType> propertyTypes;

    public PipelineNodeOutputImpl(String id, String name, PropertyType... producedType) {
        this(id, name, false, producedType);
    }

    public PipelineNodeOutputImpl(String id, String name, boolean mainConnection, PropertyType... producedType) {
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
    public List<PropertyType> getProducablePropertyTypes() {
        return propertyTypes;
    }

    @Override
    public boolean supportsMultiple() {
        return !mainConnection;
    }
}
