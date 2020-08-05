package com.gempukku.libgdx.graph.renderer.loader;

import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.google.common.base.Predicate;

public interface PipelineNodeInput {
    boolean isRequired();

    String getName();

    Predicate<PropertyType> getAcceptedTypes();
}
