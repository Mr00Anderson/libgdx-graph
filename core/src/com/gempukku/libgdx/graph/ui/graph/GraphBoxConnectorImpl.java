package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.graph.pipeline.PropertyType;
import com.google.common.base.Supplier;

public class GraphBoxConnectorImpl implements GraphBoxConnector {
    private String id;
    private Side side;
    private CommunicationType communicationType;
    private PropertyType propertyType;
    private Supplier<Float> offsetSupplier;

    public GraphBoxConnectorImpl(String id, Side side, CommunicationType communicationType, PropertyType propertyType, Supplier<Float> offsetSupplier) {
        this.id = id;
        this.side = side;
        this.communicationType = communicationType;
        this.propertyType = propertyType;
        this.offsetSupplier = offsetSupplier;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public CommunicationType getCommunicationType() {
        return communicationType;
    }

    @Override
    public PropertyType getPropertyType() {
        return propertyType;
    }

    @Override
    public float getOffset() {
        return offsetSupplier.get();
    }
}
