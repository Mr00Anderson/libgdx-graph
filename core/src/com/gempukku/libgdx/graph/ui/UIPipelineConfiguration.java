package com.gempukku.libgdx.graph.ui;

import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyColorBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyVector1BoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyVector3BoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.PropertyGraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.part.MergeBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.part.SplitBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.provided.ScreenSizeBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.provided.TimeBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.rendering.EndGraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.rendering.ObjectRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.rendering.StartGraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.rendering.UIRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueBooleanBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueColorBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueVector1BoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueVector3BoxProducer;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class UIPipelineConfiguration {
    public static Set<GraphBoxProducer> notAddableProducers = new HashSet<>();
    public static Map<String, Map<String, GraphBoxProducer>> graphBoxProducers = new LinkedHashMap<>();
    public static Map<String, PropertyBoxProducer> propertyProducers = new LinkedHashMap<>();

    static {
        Map<String, GraphBoxProducer> valueProducers = new LinkedHashMap<>();
        valueProducers.put("Color", new ValueColorBoxProducer());
        valueProducers.put("Vector1", new ValueVector1BoxProducer());
        valueProducers.put("Vector2", new ValueVector2BoxProducer());
        valueProducers.put("Vector3", new ValueVector3BoxProducer());
        valueProducers.put("Boolean", new ValueBooleanBoxProducer());
        PropertyGraphBoxProducer propertyProducer = new PropertyGraphBoxProducer();
        notAddableProducers.add(propertyProducer);
        valueProducers.put("Property", propertyProducer);
        graphBoxProducers.put("Value", valueProducers);

        Map<String, GraphBoxProducer> providedProducers = new LinkedHashMap<>();
        providedProducers.put("Time", new TimeBoxProducer());
        providedProducers.put("Screen Size", new ScreenSizeBoxProducer());
        graphBoxProducers.put("Provided", valueProducers);

        Map<String, GraphBoxProducer> mathProducers = new LinkedHashMap<>();
        mathProducers.put("Split", new SplitBoxProducer());
        mathProducers.put("Merge", new MergeBoxProducer());
        graphBoxProducers.put("Math", mathProducers);

        Map<String, GraphBoxProducer> renderingProducers = new LinkedHashMap<>();
        renderingProducers.put("Start", new StartGraphBoxProducer());
        EndGraphBoxProducer endProducer = new EndGraphBoxProducer();
        notAddableProducers.add(endProducer);
        renderingProducers.put("End", endProducer);
        renderingProducers.put("Default renderer", new ObjectRendererBoxProducer());
        renderingProducers.put("UI renderer", new UIRendererBoxProducer());
        graphBoxProducers.put("Rendering", renderingProducers);

        propertyProducers.put("Vector1", new PropertyVector1BoxProducer());
        propertyProducers.put("Vector2", new PropertyVector2BoxProducer());
        propertyProducers.put("Vector3", new PropertyVector3BoxProducer());
        propertyProducers.put("Color", new PropertyColorBoxProducer());
    }

    private UIPipelineConfiguration() {

    }
}
