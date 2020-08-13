package com.gempukku.libgdx.graph.renderer.loader.value.producer;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.renderer.config.value.ValueVector2PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.gempukku.libgdx.graph.renderer.loader.value.node.ValuePipelineNode;
import org.json.simple.JSONObject;

import java.util.Map;

public class ValueVector2PipelineNodeProducer extends PipelineNodeProducerImpl {
    public ValueVector2PipelineNodeProducer() {
        super(new ValueVector2PipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new ValuePipelineNode(configuration, "value", new Vector2(
                ((Number) data.get("v1")).floatValue(),
                ((Number) data.get("v2")).floatValue()));
    }
}
