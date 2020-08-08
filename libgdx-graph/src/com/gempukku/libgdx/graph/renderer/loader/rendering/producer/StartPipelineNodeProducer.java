package com.gempukku.libgdx.graph.renderer.loader.rendering.producer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.renderer.config.rendering.StartPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.renderer.loader.rendering.node.StartPipelineNode;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import java.util.Map;

public class StartPipelineNodeProducer extends PipelineNodeProducerImpl {
    public StartPipelineNodeProducer() {
        super(new StartPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputSuppliers) {
        return new StartPipelineNode(getConfiguration(),
                (Function<PipelineRenderingContext, Color>) inputSuppliers.get("background"),
                (Function<PipelineRenderingContext, Vector2>) inputSuppliers.get("size"));
    }
}
