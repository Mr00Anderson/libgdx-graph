package com.gempukku.libgdx.graph.ui.shader;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.EndShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.value.ValueBooleanShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.value.ValueColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.value.ValueFloatShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.value.ValueVector2ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.value.ValueVector3ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.producer.value.ValueBooleanBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueColorBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueVector3BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.property.PropertyColorBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.property.PropertyFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.property.PropertyVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.property.PropertyVector3BoxProducer;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class UIShaderConfiguration implements UIGraphConfiguration<ShaderFieldType> {
    public static Set<GraphBoxProducer<ShaderFieldType>> notAddableProducers = new HashSet<>();
    public static Map<String, Set<GraphBoxProducer<ShaderFieldType>>> graphBoxProducers = new LinkedHashMap<>();
    public static Map<String, PropertyBoxProducer<ShaderFieldType>> propertyProducers = new LinkedHashMap<>();

    static {
        Set<GraphBoxProducer<ShaderFieldType>> valueProducers = new LinkedHashSet<>();
        valueProducers.add(new ValueColorBoxProducer<ShaderFieldType>(new ValueColorShaderNodeConfiguration()));
        valueProducers.add(new ValueFloatBoxProducer<ShaderFieldType>(new ValueFloatShaderNodeConfiguration()));
        valueProducers.add(new ValueVector2BoxProducer<ShaderFieldType>(new ValueVector2ShaderNodeConfiguration()));
        valueProducers.add(new ValueVector3BoxProducer<ShaderFieldType>(new ValueVector3ShaderNodeConfiguration()));
        valueProducers.add(new ValueBooleanBoxProducer<ShaderFieldType>(new ValueBooleanShaderNodeConfiguration()));
        PropertyShaderGraphBoxProducer propertyProducer = new PropertyShaderGraphBoxProducer();
        notAddableProducers.add(propertyProducer);
        valueProducers.add(propertyProducer);
        // Put it wherever...
        GraphBoxProducer<ShaderFieldType> endProducer = new GraphBoxProducerImpl<ShaderFieldType>(new EndShaderNodeConfiguration()) {
            @Override
            public boolean isCloseable() {
                return false;
            }
        };
        notAddableProducers.add(endProducer);
        valueProducers.add(endProducer);
        graphBoxProducers.put("Value", valueProducers);

        propertyProducers.put("Float", new PropertyFloatBoxProducer());
        propertyProducers.put("Vector2", new PropertyVector2BoxProducer());
        propertyProducers.put("Vector3", new PropertyVector3BoxProducer());
        propertyProducers.put("Color", new PropertyColorBoxProducer());
    }

    @Override
    public Map<String, Set<GraphBoxProducer<ShaderFieldType>>> getGraphBoxProducers() {
        return graphBoxProducers;
    }

    @Override
    public Map<String, PropertyBoxProducer<ShaderFieldType>> getPropertyBoxProducers() {
        return propertyProducers;
    }

    @Override
    public boolean isAddableGraphBox(GraphBoxProducer<ShaderFieldType> graphBoxProducer) {
        return !notAddableProducers.contains(graphBoxProducer);
    }
}
