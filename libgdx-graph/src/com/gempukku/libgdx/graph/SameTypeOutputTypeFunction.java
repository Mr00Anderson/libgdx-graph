package com.gempukku.libgdx.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.util.Map;

public class SameTypeOutputTypeFunction<T extends FieldType> implements Function<Map<String, T>, T> {
    private String input;

    public SameTypeOutputTypeFunction(String input) {
        this.input = input;
    }

    @Override
    public T apply(@Nullable Map<String, T> inputs) {
        return inputs.get(input);
    }
}
