package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;

import java.util.Arrays;
import java.util.List;

public class PipelineNodeInputImpl implements PipelineNodeInput {
    private String id;
    private String name;
    private boolean required;
    private boolean mainConnection;

    private InputPropertyType inputPropertyType;

    public PipelineNodeInputImpl(String id, String name, PropertyType acceptedType) {
        this(id, name, Arrays.asList(acceptedType), false, false);
    }

    public PipelineNodeInputImpl(String id, String name, List<PropertyType> acceptedTypes) {
        this(id, name, acceptedTypes, false, false);
    }

    public PipelineNodeInputImpl(String id, String name, final List<PropertyType> acceptedTypes, boolean required, boolean mainConnection) {
        this.id = id;
        this.name = name;
        this.required = required;
        this.mainConnection = mainConnection;

        inputPropertyType = new InputPropertyType() {
            @Override
            public List<PropertyType> getAcceptedPropertyTypes() {
                return acceptedTypes;
            }
        };
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
    public InputPropertyType getPropertyType() {
        return inputPropertyType;
    }
}
