package com.gempukku.libgdx.graph.ui;

import com.gempukku.libgdx.graph.renderer.config.math.AddPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.math.MultiplyPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.math.SubtractPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.part.MergePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.part.SplitPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.postprocessor.BloomPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.postprocessor.GammaCorrectionPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.postprocessor.GaussianBlurPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.provided.RenderSizePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.provided.TimePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.rendering.DefaultRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.rendering.EndPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.rendering.PipelineRendererNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.rendering.StartPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.rendering.UIRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.value.ValueBooleanPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.value.ValueColorPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.value.ValueFloatPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.value.ValueVector2PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.config.value.ValueVector3PipelineNodeConfiguration;
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
    public static Set<GraphBoxProducer> notAddableProducers = new HashSet<>();
    public static Map<String, Set<GraphBoxProducer>> graphBoxProducers = new LinkedHashMap<>();
    public static Map<String, PropertyBoxProducer> propertyProducers = new LinkedHashMap<>();

    static {
        Set<GraphBoxProducer> valueProducers = new LinkedHashSet<>();
        valueProducers.add(new ValueColorBoxProducer(new ValueColorPipelineNodeConfiguration()));
        valueProducers.add(new ValueFloatBoxProducer(new ValueFloatPipelineNodeConfiguration()));
        valueProducers.add(new ValueVector2BoxProducer(new ValueVector2PipelineNodeConfiguration()));
        valueProducers.add(new ValueVector3BoxProducer(new ValueVector3PipelineNodeConfiguration()));
        valueProducers.add(new ValueBooleanBoxProducer(new ValueBooleanPipelineNodeConfiguration()));
        PropertyGraphBoxProducer propertyProducer = new PropertyGraphBoxProducer();
        notAddableProducers.add(propertyProducer);
        valueProducers.add(propertyProducer);
        graphBoxProducers.put("Value", valueProducers);

        Set<GraphBoxProducer> providedProducers = new LinkedHashSet<>();
        providedProducers.add(new GraphBoxProducerImpl(new TimePipelineNodeConfiguration()));
        providedProducers.add(new GraphBoxProducerImpl(new RenderSizePipelineNodeConfiguration()));
        graphBoxProducers.put("Provided", providedProducers);

        Set<GraphBoxProducer> mathProducers = new LinkedHashSet<>();
        mathProducers.add(new GraphBoxProducerImpl(new AddPipelineNodeConfiguration()));
        mathProducers.add(new GraphBoxProducerImpl(new SubtractPipelineNodeConfiguration()));
        mathProducers.add(new GraphBoxProducerImpl(new MultiplyPipelineNodeConfiguration()));
        mathProducers.add(new GraphBoxProducerImpl(new SplitPipelineNodeConfiguration()));
        mathProducers.add(new GraphBoxProducerImpl(new MergePipelineNodeConfiguration()));
        graphBoxProducers.put("Math", mathProducers);

        Set<GraphBoxProducer> renderingProducers = new LinkedHashSet<>();
        renderingProducers.add(new GraphBoxProducerImpl(new StartPipelineNodeConfiguration()));
        GraphBoxProducer endProducer = new GraphBoxProducerImpl(new EndPipelineNodeConfiguration()) {
            @Override
            public boolean isCloseable() {
                return false;
            }
        };
        notAddableProducers.add(endProducer);
        renderingProducers.add(endProducer);
        renderingProducers.add(new GraphBoxProducerImpl(new DefaultRendererPipelineNodeConfiguration()));
        renderingProducers.add(new GraphBoxProducerImpl(new UIRendererPipelineNodeConfiguration()));
        renderingProducers.add(new GraphBoxProducerImpl(new PipelineRendererNodeConfiguration()));
        graphBoxProducers.put("Rendering", renderingProducers);

        Set<GraphBoxProducer> postProcessorProducers = new LinkedHashSet<>();
        postProcessorProducers.add(new GraphBoxProducerImpl(new BloomPipelineNodeConfiguration()));
        postProcessorProducers.add(new GraphBoxProducerImpl(new GaussianBlurPipelineNodeConfiguration()));
        postProcessorProducers.add(new GraphBoxProducerImpl(new GammaCorrectionPipelineNodeConfiguration()));
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
