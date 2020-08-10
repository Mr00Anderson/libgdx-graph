package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;

import java.util.Arrays;
import java.util.List;

public class PipelineNodeOutputImpl implements PipelineNodeOutput {
    private String id;
    private String name;
    private boolean mainConnection;
    private OutputPropertyType outputPropertyType;

    public PipelineNodeOutputImpl(String id, String name, PropertyType producedType) {
        this(id, name, Arrays.asList(producedType), false);
    }

    public PipelineNodeOutputImpl(String id, String name, List<PropertyType> possibleProducedTypes) {
        this(id, name, possibleProducedTypes, false);
    }

    public PipelineNodeOutputImpl(String id, String name, final List<PropertyType> possibleProducedTypes, boolean mainConnection) {
        this.id = id;
        this.name = name;
        this.mainConnection = mainConnection;

        outputPropertyType = new OutputPropertyType() {
            @Override
            public boolean mayProduce(PropertyType propertyType) {
                return possibleProducedTypes.contains(propertyType);
            }

            @Override
            public PropertyType determinePropertyType() {
                throw new UnsupportedOperationException("To be implemented");
            }
        };
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
    public OutputPropertyType getPropertyType() {
        return outputPropertyType;
    }
}
