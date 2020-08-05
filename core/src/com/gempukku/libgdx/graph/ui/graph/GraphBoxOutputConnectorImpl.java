package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.google.common.base.Supplier;

public class GraphBoxOutputConnectorImpl implements GraphBoxOutputConnector {
    private String id;
    private Side side;
    private PropertyType propertyType;
    private Supplier<Float> offsetSupplier;

    public GraphBoxOutputConnectorImpl(String id, Side side, PropertyType propertyType, Supplier<Float> offsetSupplier) {
        this.id = id;
        this.side = side;
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
    public PropertyType getPropertyType() {
        return propertyType;
    }

    @Override
    public float getOffset() {
        return offsetSupplier.get();
    }
}
