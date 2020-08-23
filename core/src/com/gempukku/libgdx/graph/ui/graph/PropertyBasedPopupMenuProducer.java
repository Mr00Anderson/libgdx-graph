package com.gempukku.libgdx.graph.ui.graph;

import com.gempukku.libgdx.graph.data.FieldType;
import com.kotcrab.vis.ui.widget.PopupMenu;

public interface PropertyBasedPopupMenuProducer<T extends FieldType> {
    PopupMenu createPopupMenu(Iterable<? extends PropertyProducer<T>> propertyProducers, float x, float y);
}
