package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.google.common.base.Predicate;
import com.google.common.base.Supplier;

public class GraphBoxInputConnectorImpl implements GraphBoxInputConnector {
    private String id;
    private Side side;
    private Predicate<PropertyType> propertyPredicate;
    private Supplier<Float> offsetSupplier;

    public GraphBoxInputConnectorImpl(String id, Side side, Predicate<PropertyType> propertyPredicate, Supplier<Float> offsetSupplier) {
        this.id = id;
        this.side = side;
        this.propertyPredicate = propertyPredicate;
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
    public boolean accepts(PropertyType propertyType) {
        return propertyPredicate.apply(propertyType);
    }

    @Override
    public float getOffset() {
        return offsetSupplier.get();
    }
}
