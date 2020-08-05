package com.gempukku.libgdx.graph.renderer.loader.value.node;

import com.gempukku.libgdx.graph.renderer.loader.PipelineNode;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class ValuePipelineNode implements PipelineNode {
    private String propertyName;
    private Supplier<Object> value;

    public ValuePipelineNode(String propertyName, Object value) {
        this.propertyName = propertyName;
        this.value = Suppliers.ofInstance(value);
    }

    @Override
    public Supplier<?> getOutputSupplier(String name) {
        if (!name.equals(propertyName))
            throw new IllegalArgumentException();
        return value;
    }

    @Override
    public void startFrame() {

    }

    @Override
    public void endFrame() {

    }

    @Override
    public void dispose() {

    }
}
