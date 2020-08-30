package com.gempukku.libgdx.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.util.Map;

public class VectorArithmeticOutputTypeFunction<T extends FieldType> implements Function<Map<String, T>, T> {
    private T floatType;
    private String input1;
    private String input2;

    public VectorArithmeticOutputTypeFunction(T floatType, String input1, String input2) {
        this.floatType = floatType;
        this.input1 = input1;
        this.input2 = input2;
    }

    @Override
    public T apply(@Nullable Map<String, T> inputs) {
        T a = inputs.get(input1);
        T b = inputs.get(input2);
        if (a == null || b == null)
            return null;

        if (a == floatType)
            return b;
        if (b == floatType)
            return a;
        if (a != b)
            return null;

        return a;
    }
}
