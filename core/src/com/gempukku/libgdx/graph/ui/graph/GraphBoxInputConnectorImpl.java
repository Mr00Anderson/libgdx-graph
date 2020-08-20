package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.google.common.base.Supplier;

import java.util.List;

public class GraphBoxInputConnectorImpl<T extends FieldType> implements GraphBoxInputConnector<T> {
    private Side side;
    private Supplier<Float> offsetSupplier;
    private GraphNodeInput<T> graphNodeInput;

    public GraphBoxInputConnectorImpl(Side side, Supplier<Float> offsetSupplier, GraphNodeInput<T> graphNodeInput) {
        this.side = side;
        this.offsetSupplier = offsetSupplier;
        this.graphNodeInput = graphNodeInput;
    }

    @Override
    public Side getSide() {
        return side;
    }

    @Override
    public float getOffset() {
        return offsetSupplier.get();
    }

    @Override
    public boolean isRequired() {
        return graphNodeInput.isRequired();
    }

    @Override
    public boolean isMainConnection() {
        return graphNodeInput.isMainConnection();
    }

    @Override
    public String getFieldName() {
        return graphNodeInput.getFieldName();
    }

    @Override
    public String getFieldId() {
        return graphNodeInput.getFieldId();
    }

    @Override
    public List<? extends T> getAcceptedPropertyTypes() {
        return graphNodeInput.getAcceptedPropertyTypes();
    }
}
