package com.gempukku.libgdx.graph.pipeline.loader.node;

import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;
import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GraphNodeOutputImpl<T extends FieldType> implements GraphNodeOutput<T> {
    private String id;
    private String name;
    private boolean mainConnection;
    private Function<Map<String, T>, T> outputTypeFunction;
    private List<? extends T> propertyTypes;

    public GraphNodeOutputImpl(String id, String name, final T producedType) {
        this(id, name, false, producedType);
    }

    public GraphNodeOutputImpl(String id, String name, boolean mainConnection, final T producedType) {
        this(id, name, mainConnection, new Function<Map<String, T>, T>() {
            @Override
            public T apply(@Nullable Map<String, T> stringTMap) {
                return producedType;
            }
        }, producedType);
    }

    public GraphNodeOutputImpl(String id, String name, Function<Map<String, T>, T> outputTypeFunction, T... producedType) {
        this(id, name, false, outputTypeFunction, producedType);
    }

    public GraphNodeOutputImpl(String id, String name, boolean mainConnection, Function<Map<String, T>, T> outputTypeFunction, T... producedType) {
        this.id = id;
        this.name = name;
        this.mainConnection = mainConnection;
        this.outputTypeFunction = outputTypeFunction;
        this.propertyTypes = Arrays.asList(producedType);
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
    public List<? extends T> getProducableFieldTypes() {
        return propertyTypes;
    }

    @Override
    public boolean supportsMultiple() {
        return !mainConnection;
    }

    @Override
    public T determineFieldType(Map<String, T> inputs) {
        return outputTypeFunction.apply(inputs);
    }
}
