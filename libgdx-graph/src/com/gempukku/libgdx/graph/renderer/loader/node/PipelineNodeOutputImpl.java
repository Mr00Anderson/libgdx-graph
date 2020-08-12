package com.gempukku.libgdx.graph.renderer.loader.node;

import com.gempukku.libgdx.graph.renderer.PropertyType;

public class PipelineNodeOutputImpl implements PipelineNodeOutput {
    private String id;
    private String name;
    private boolean mainConnection;
    private OutputPropertyType outputPropertyType;

    public PipelineNodeOutputImpl(String id, String name, PropertyType producedType) {
        this(id, name, producedType, false);
    }

    public PipelineNodeOutputImpl(String id, String name, final PropertyType propertyType, boolean mainConnection) {
        this.id = id;
        this.name = name;
        this.mainConnection = mainConnection;

        outputPropertyType = new OutputPropertyType() {
            @Override
            public boolean mayProduce(PropertyType propertyTypeCompare) {
                return propertyType == propertyTypeCompare;
            }

            @Override
            public PropertyType determinePropertyType() {
                return propertyType;
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

    @Override
    public boolean supportsMultiple() {
        return !mainConnection;
    }
}
