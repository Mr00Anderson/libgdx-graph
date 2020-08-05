package com.gempukku.libgdx.graph.renderer.loader.rendering;

import com.google.common.base.Supplier;

public class WorkerSupplier<T> implements Supplier<T> {
    private T value;
    private boolean hasValue;
    private Runnable workExecutor;

    public WorkerSupplier(Runnable workExecutor) {
        this.workExecutor = workExecutor;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void resetValue() {
        hasValue = false;
        value = null;
    }

    @Override
    public T get() {
        if (!hasValue) {
            workExecutor.run();
        }
        return value;
    }
}
