package com.gempukku.libgdx.graph.renderer;

import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.property.PropertyPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.producer.DefaultRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.producer.EndPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.producer.StartPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.producer.UIRendererPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.provided.ScreenSizePipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.value.producer.ValueBooleanPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.value.producer.ValueColorPipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.value.producer.ValueVector1PipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.value.producer.ValueVector2PipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.value.producer.ValueVector3PipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.property.CameraPipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.ColorPipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.LightsPipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.ModelsPipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.PipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.StagePipelinePropertyProducer;
import com.gempukku.libgdx.graph.renderer.property.Vector1PipelinePropertyProducer;
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
        pipelineNodeProducers.put("PipelineStart", new StartPipelineNodeProducer());
        pipelineNodeProducers.put("PipelineEnd", new EndPipelineNodeProducer());
        pipelineNodeProducers.put("UIRenderer", new UIRendererPipelineNodeProducer());
        pipelineNodeProducers.put("DefaultRenderer", new DefaultRendererPipelineNodeProducer());

        pipelineNodeProducers.put("ValueVector1", new ValueVector1PipelineNodeProducer());
        pipelineNodeProducers.put("ValueVector2", new ValueVector2PipelineNodeProducer());
        pipelineNodeProducers.put("ValueVector3", new ValueVector3PipelineNodeProducer());
        pipelineNodeProducers.put("ValueColor", new ValueColorPipelineNodeProducer());
        pipelineNodeProducers.put("ValueBoolean", new ValueBooleanPipelineNodeProducer());

        pipelineNodeProducers.put("ScreenSize", new ScreenSizePipelineNodeProducer());

        pipelineNodeProducers.put("Property", new PropertyPipelineNodeProducer());

        pipelinePropertyProducers.add(new Vector1PipelinePropertyProducer());
        pipelinePropertyProducers.add(new Vector2PipelinePropertyProducer());
        pipelinePropertyProducers.add(new Vector3PipelinePropertyProducer());
        pipelinePropertyProducers.add(new ColorPipelinePropertyProducer());
        pipelinePropertyProducers.add(new StagePipelinePropertyProducer());
        pipelinePropertyProducers.add(new ModelsPipelinePropertyProducer());
        pipelinePropertyProducers.add(new LightsPipelinePropertyProducer());
        pipelinePropertyProducers.add(new CameraPipelinePropertyProducer());
    }

    private RendererPipelineConfiguration() {

    }
}
