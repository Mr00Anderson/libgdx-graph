package com.gempukku.libgdx.graph.ui;

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
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyCameraBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyColorBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyLightsBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyModelsBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyStageBoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyVector3BoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.producer.PropertyGraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.shader.GraphShaderRendererBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueBooleanBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueColorBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueVector3BoxProducer;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class UIPipelineConfiguration {
    public static Set<GraphBoxProducer<PipelineFieldType>> notAddableProducers = new HashSet<>();
    public static Map<String, Set<GraphBoxProducer<PipelineFieldType>>> graphBoxProducers = new LinkedHashMap<>();
    public static Map<String, PropertyBoxProducer<PipelineFieldType>> propertyProducers = new LinkedHashMap<>();

    static {
        Set<GraphBoxProducer<PipelineFieldType>> valueProducers = new LinkedHashSet<>();
        valueProducers.add(new ValueColorBoxProducer<PipelineFieldType>(new ValueColorPipelineNodeConfiguration()));
        valueProducers.add(new ValueFloatBoxProducer<PipelineFieldType>(new ValueFloatPipelineNodeConfiguration()));
        valueProducers.add(new ValueVector2BoxProducer<PipelineFieldType>(new ValueVector2PipelineNodeConfiguration()));
        valueProducers.add(new ValueVector3BoxProducer<PipelineFieldType>(new ValueVector3PipelineNodeConfiguration()));
        valueProducers.add(new ValueBooleanBoxProducer<PipelineFieldType>(new ValueBooleanPipelineNodeConfiguration()));
        PropertyGraphBoxProducer propertyProducer = new PropertyGraphBoxProducer();
        notAddableProducers.add(propertyProducer);
        valueProducers.add(propertyProducer);
        graphBoxProducers.put("Value", valueProducers);

        Set<GraphBoxProducer<PipelineFieldType>> providedProducers = new LinkedHashSet<>();
        providedProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new TimePipelineNodeConfiguration()));
        providedProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new RenderSizePipelineNodeConfiguration()));
        graphBoxProducers.put("Provided", providedProducers);

        Set<GraphBoxProducer<PipelineFieldType>> mathProducers = new LinkedHashSet<>();
        mathProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new AddPipelineNodeConfiguration()));
        mathProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new SubtractPipelineNodeConfiguration()));
        mathProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new MultiplyPipelineNodeConfiguration()));
        mathProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new SplitPipelineNodeConfiguration()));
        mathProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new MergePipelineNodeConfiguration()));
        graphBoxProducers.put("Math", mathProducers);

        Set<GraphBoxProducer<PipelineFieldType>> renderingProducers = new LinkedHashSet<>();
        renderingProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new StartPipelineNodeConfiguration()));
        GraphBoxProducer<PipelineFieldType> endProducer = new GraphBoxProducerImpl<PipelineFieldType>(new EndPipelineNodeConfiguration()) {
            @Override
            public boolean isCloseable() {
                return false;
            }
        };
        notAddableProducers.add(endProducer);
        renderingProducers.add(endProducer);
        renderingProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new DefaultRendererPipelineNodeConfiguration()));
        renderingProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new UIRendererPipelineNodeConfiguration()));
        renderingProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new PipelineRendererNodeConfiguration()));
        renderingProducers.add(new GraphShaderRendererBoxProducer());
        graphBoxProducers.put("Rendering", renderingProducers);

        Set<GraphBoxProducer<PipelineFieldType>> postProcessorProducers = new LinkedHashSet<>();
        postProcessorProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new BloomPipelineNodeConfiguration()));
        postProcessorProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new GaussianBlurPipelineNodeConfiguration()));
        postProcessorProducers.add(new GraphBoxProducerImpl<PipelineFieldType>(new GammaCorrectionPipelineNodeConfiguration()));
        graphBoxProducers.put("Post Processor", postProcessorProducers);

        propertyProducers.put("Float", new PropertyFloatBoxProducer());
        propertyProducers.put("Vector2", new PropertyVector2BoxProducer());
        propertyProducers.put("Vector3", new PropertyVector3BoxProducer());
        propertyProducers.put("Color", new PropertyColorBoxProducer());
        propertyProducers.put("Stage", new PropertyStageBoxProducer());
        propertyProducers.put("Models", new PropertyModelsBoxProducer());
        propertyProducers.put("Lights", new PropertyLightsBoxProducer());
        propertyProducers.put("Camera", new PropertyCameraBoxProducer());
    }

    private UIPipelineConfiguration() {

    }
}
