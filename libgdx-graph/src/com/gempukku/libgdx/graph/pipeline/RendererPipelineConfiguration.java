package com.gempukku.libgdx.graph.pipeline;

import com.gempukku.libgdx.graph.pipeline.loader.math.AddPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.math.MultiplyPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.math.SubtractPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.part.MergePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.part.SplitPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.postprocessor.BloomPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.postprocessor.GammaCorrectionPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.postprocessor.GaussianBlurPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.property.PropertyPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.provided.RenderSizePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.provided.TimePipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.DefaultRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.EndPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.GraphShaderRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.PipelineRendererNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.StartPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.producer.UIRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.value.producer.ValueBooleanPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.value.producer.ValueColorPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.value.producer.ValueFloatPipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.value.producer.ValueVector2PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.value.producer.ValueVector3PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.property.CameraPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.ColorPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.FloatPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.LightsPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.ModelsPipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.PipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.StagePipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.Vector2PipelinePropertyProducer;
import com.gempukku.libgdx.graph.pipeline.property.Vector3PipelinePropertyProducer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RendererPipelineConfiguration {
    public static Map<String, PipelineNodeProducer> pipelineNodeProducers = new HashMap<>();
    public static List<PipelinePropertyProducer> pipelinePropertyProducers = new LinkedList<>();

    static {
        addNodeProducer(new StartPipelineNodeProducer());
        addNodeProducer(new EndPipelineNodeProducer());
        addNodeProducer(new UIRendererPipelineNodeProducer());
        addNodeProducer(new DefaultRendererPipelineNodeProducer());
        addNodeProducer(new GraphShaderRendererPipelineNodeProducer());
        addNodeProducer(new PipelineRendererNodeProducer());

        addNodeProducer(new ValueFloatPipelineNodeProducer());
        addNodeProducer(new ValueVector2PipelineNodeProducer());
        addNodeProducer(new ValueVector3PipelineNodeProducer());
        addNodeProducer(new ValueColorPipelineNodeProducer());
        addNodeProducer(new ValueBooleanPipelineNodeProducer());

        addNodeProducer(new RenderSizePipelineNodeProducer());
        addNodeProducer(new TimePipelineNodeProducer());

        addNodeProducer(new AddPipelineNodeProducer());
        addNodeProducer(new SubtractPipelineNodeProducer());
        addNodeProducer(new MultiplyPipelineNodeProducer());
        addNodeProducer(new MergePipelineNodeProducer());
        addNodeProducer(new SplitPipelineNodeProducer());

        addNodeProducer(new PropertyPipelineNodeProducer());

        addNodeProducer(new BloomPipelineNodeProducer());
        addNodeProducer(new GaussianBlurPipelineNodeProducer());
        addNodeProducer(new GammaCorrectionPipelineNodeProducer());

        pipelinePropertyProducers.add(new FloatPipelinePropertyProducer());
        pipelinePropertyProducers.add(new Vector2PipelinePropertyProducer());
        pipelinePropertyProducers.add(new Vector3PipelinePropertyProducer());
        pipelinePropertyProducers.add(new ColorPipelinePropertyProducer());
        pipelinePropertyProducers.add(new StagePipelinePropertyProducer());
        pipelinePropertyProducers.add(new ModelsPipelinePropertyProducer());
        pipelinePropertyProducers.add(new LightsPipelinePropertyProducer());
        pipelinePropertyProducers.add(new CameraPipelinePropertyProducer());
    }

    private static void addNodeProducer(PipelineNodeProducer producer) {
        pipelineNodeProducers.put(producer.getType(), producer);
    }

    private RendererPipelineConfiguration() {

    }
}
