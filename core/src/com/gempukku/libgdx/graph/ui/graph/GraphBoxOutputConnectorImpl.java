package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;
import com.google.common.base.Supplier;

import java.util.List;

public class GraphBoxOutputConnectorImpl<T extends FieldType> implements GraphBoxOutputConnector<T> {
    private Side side;
    private Supplier<Float> offsetSupplier;
    private GraphNodeOutput<T> graphNodeOutput;

    public GraphBoxOutputConnectorImpl(Side side, Supplier<Float> offsetSupplier, GraphNodeOutput<T> graphNodeOutput) {
        this.side = side;
        this.offsetSupplier = offsetSupplier;
        this.graphNodeOutput = graphNodeOutput;
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
    public boolean isMainConnection() {
        return graphNodeOutput.isMainConnection();
    }

    @Override
    public String getFieldName() {
        return graphNodeOutput.getFieldName();
    }

    @Override
    public String getFieldId() {
        return graphNodeOutput.getFieldId();
    }

    @Override
    public List<? extends T> getProducablePropertyTypes() {
        return graphNodeOutput.getProducablePropertyTypes();
    }

    @Override
    public boolean supportsMultiple() {
        return graphNodeOutput.supportsMultiple();
    }
}
