package com.gempukku.libgdx.graph.renderer.loader.rendering.producer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.PipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.node.StartPipelineNode;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import org.json.simple.JSONObject;

import java.util.Map;

public class StartPipelineNodeProducer implements PipelineNodeProducer {
    public static final String type = "PipelineStart";
    private PipelineNodeConfigurationImpl configuration = new PipelineNodeConfigurationImpl();

    public StartPipelineNodeProducer() {
        configuration.addNodeInput(
                new PipelineNodeInputImpl(false, "background", Predicates.equalTo(PropertyType.Color)));
        configuration.addNodeInput(
                new PipelineNodeInputImpl(false, "size", Predicates.equalTo(PropertyType.Vector2)));
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("output", PropertyType.RenderPipeline));
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals(type);
    }

    @Override
    public PipelineNodeConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Supplier<?>> inputSuppliers) {
        return new StartPipelineNode(
                (Supplier<Color>) inputSuppliers.get("background"),
                (Supplier<Vector2>) inputSuppliers.get("size"));
    }
}
