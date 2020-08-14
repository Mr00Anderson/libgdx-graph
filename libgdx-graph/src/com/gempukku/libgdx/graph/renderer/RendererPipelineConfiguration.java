package com.gempukku.libgdx.graph.renderer;

import com.gempukku.libgdx.graph.renderer.loader.math.AddPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.math.MultiplyPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.part.MergePipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.part.SplitPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.postprocessor.BloomPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.postprocessor.GammaCorrectionPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.postprocessor.GaussianBlurPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.property.PropertyPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.provided.RenderSizePipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.provided.TimePipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.producer.DefaultRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.producer.EndPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.producer.PipelineRendererNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.producer.StartPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.producer.UIRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.value.producer.ValueBooleanPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.value.producer.ValueColorPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.value.producer.ValueFloatPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.value.producer.ValueVector2PipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.value.producer.ValueVector3PipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.property.CameraPipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.ColorPipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.FloatPipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.LightsPipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.ModelsPipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.PipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.StagePipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.Vector2PipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.Vector3PipelinePropertyProducer;

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
        addNodeProducer(new PipelineRendererNodeProducer());

        addNodeProducer(new ValueFloatPipelineNodeProducer());
        addNodeProducer(new ValueVector2PipelineNodeProducer());
        addNodeProducer(new ValueVector3PipelineNodeProducer());
        addNodeProducer(new ValueColorPipelineNodeProducer());
        addNodeProducer(new ValueBooleanPipelineNodeProducer());

        addNodeProducer(new RenderSizePipelineNodeProducer());
        addNodeProducer(new TimePipelineNodeProducer());

        addNodeProducer(new AddPipelineNodeProducer());
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
