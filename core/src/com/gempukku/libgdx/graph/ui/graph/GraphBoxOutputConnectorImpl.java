package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

public class GraphBoxOutputConnectorImpl implements GraphBoxOutputConnector {
    private String id;
    private Side side;
    private Predicate<PropertyType> possiblePropertyTypes;
    private Supplier<Float> offsetSupplier;
    private boolean supportsMultiple;

    public GraphBoxOutputConnectorImpl(String id, Side side, Predicate<PropertyType> possiblePropertyTypes, Supplier<Float> offsetSupplier) {
        this(id, side, possiblePropertyTypes, offsetSupplier, true);
    }

    public GraphBoxOutputConnectorImpl(String id, Side side, Predicate<PropertyType> possiblePropertyTypes, Supplier<Float> offsetSupplier, boolean supportsMultiple) {
        this.id = id;
        this.side = side;
        this.possiblePropertyTypes = possiblePropertyTypes;
        this.offsetSupplier = offsetSupplier;
        this.supportsMultiple = supportsMultiple;
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
    public boolean produces(PropertyType propertyType) {
        return possiblePropertyTypes.apply(propertyType);
    }

    @Override
    public float getOffset() {
        return offsetSupplier.get();
    }

    @Override
    public boolean supportsMultiple() {
        return supportsMultiple;
    }
}
