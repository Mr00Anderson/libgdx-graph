package com.gempukku.libgdx.graph.ui.pipeline;

import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.config.math.AddPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.MultiplyPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.math.SubtractPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.part.MergePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.part.SplitPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.BloomPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.GammaCorrectionPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.postprocessor.GaussianBlurPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.provided.RenderSizePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.provided.TimePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.DefaultRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.EndPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.PipelineRendererNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.StartPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.rendering.UIRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueBooleanPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueColorPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueFloatPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueVector2PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.config.value.ValueVector3PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyCameraBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyColorBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyLightsBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyModelsBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyStageBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyVector3BoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.shader.GraphShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.producer.value.ValueBooleanBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueColorBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueVector3BoxProducer;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class UIPipelineConfiguration implements UIGraphConfiguration<PipelineFieldType> {
    private static Set<GraphBoxProducer<PipelineFieldType>> graphBoxProducers = new LinkedHashSet<>();
    private static Map<String, PropertyBoxProducer<PipelineFieldType>> propertyProducers = new LinkedHashMap<>();

    static {
        GraphBoxProducer<PipelineFieldType> endProducer = new GraphBoxProducerImpl<PipelineFieldType>(new EndPipelineNodeConfiguration()) {
            @Override
            public boolean isCloseable() {
                return false;
            }
        };
        graphBoxProducers.add(endProducer);

        graphBoxProducers.add(new PropertyPipelineGraphBoxProducer());

        graphBoxProducers.add(new ValueColorBoxProducer<PipelineFieldType>(new ValueColorPipelineNodeConfiguration()));
        graphBoxProducers.add(new ValueFloatBoxProducer<PipelineFieldType>(new ValueFloatPipelineNodeConfiguration()));
        graphBoxProducers.add(new ValueVector2BoxProducer<PipelineFieldType>(new ValueVector2PipelineNodeConfiguration()));
        graphBoxProducers.add(new ValueVector3BoxProducer<PipelineFieldType>(new ValueVector3PipelineNodeConfiguration()));
        graphBoxProducers.add(new ValueBooleanBoxProducer<PipelineFieldType>(new ValueBooleanPipelineNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new TimePipelineNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new RenderSizePipelineNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new AddPipelineNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new SubtractPipelineNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new MultiplyPipelineNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new SplitPipelineNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new MergePipelineNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new StartPipelineNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new DefaultRendererPipelineNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new UIRendererPipelineNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new PipelineRendererNodeConfiguration()));
        graphBoxProducers.add(new GraphShaderRendererBoxProducer());

        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new BloomPipelineNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new GaussianBlurPipelineNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new GammaCorrectionPipelineNodeConfiguration()));

        propertyProducers.put("Float", new PropertyFloatBoxProducer());
        propertyProducers.put("Vector2", new PropertyVector2BoxProducer());
        propertyProducers.put("Vector3", new PropertyVector3BoxProducer());
        propertyProducers.put("Color", new PropertyColorBoxProducer());
        propertyProducers.put("Stage", new PropertyStageBoxProducer());
        propertyProducers.put("Models", new PropertyModelsBoxProducer());
        propertyProducers.put("Lights", new PropertyLightsBoxProducer());
        propertyProducers.put("Camera", new PropertyCameraBoxProducer());
    }

    @Override
    public Set<GraphBoxProducer<PipelineFieldType>> getGraphBoxProducers() {
        return graphBoxProducers;
    }

    @Override
    public Map<String, PropertyBoxProducer<PipelineFieldType>> getPropertyBoxProducers() {
        return propertyProducers;
    }
}
