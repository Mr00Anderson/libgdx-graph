package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;

import java.util.Arrays;
import java.util.List;

public class PipelineNodeInputImpl implements PipelineNodeInput {
    private String id;
    private String name;
    private List<PropertyType> acceptedTypes;
    private boolean required;
    private boolean mainConnection;

    public PipelineNodeInputImpl(String id, String name, PropertyType... acceptedType) {
        this(id, name, false, acceptedType);
    }

    public PipelineNodeInputImpl(String id, String name, boolean required, PropertyType... acceptedType) {
        this(id, name, required, false, acceptedType);
    }

    public PipelineNodeInputImpl(String id, String name, boolean required, boolean mainConnection, PropertyType... acceptedType) {
        this.id = id;
        this.name = name;
        this.required = required;
        this.mainConnection = mainConnection;
        this.acceptedTypes = Arrays.asList(acceptedType);
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public boolean isMainConnection() {
        return mainConnection;
    }

    @Override
    public String getFieldId() {
        return id;
    }

    @Override
    public String getFieldName() {
        return name;
    }

    @Override
    public List<PropertyType> getAcceptedPropertyTypes() {
        return acceptedTypes;
    }
}
