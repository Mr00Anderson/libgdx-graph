package com.gempukku.libgdx.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.google.common.base.Function;

import javax.annotation.Nullable;
import java.util.Map;

public class MathCommonOutputTypeFunction<T extends FieldType> implements Function<Map<String, T>, T> {
    private T floatType;
    private String[] types;
    private String[] floatAccepting;

    public MathCommonOutputTypeFunction(T floatType, String[] types, String[] floatAccepting) {
        this.floatType = floatType;
        this.types = types;
        this.floatAccepting = floatAccepting;
    }

    @Override
    public T apply(@Nullable Map<String, T> map) {
        T resolvedType = null;
        for (String input : types) {
            T type = map.get(input);
            if (type == null)
                return null;
            if (resolvedType != null && resolvedType != type)
                return null;
            resolvedType = type;
        }

        T floatResolvedType = null;
        for (String maybeFloat : floatAccepting) {
            T type = map.get(maybeFloat);
            if (type == null)
                return null;
            if (type != floatType && type != resolvedType)
                return null;
            if (floatResolvedType != null && floatResolvedType != type)
                return null;
            floatResolvedType = type;
        }

        return resolvedType;
    }
}
