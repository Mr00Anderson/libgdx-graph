package com.gempukku.libgdx.graph.renderer.loader.part;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducer;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.Map;

public class SplitPipelineNodeProducer implements PipelineNodeProducer {
    private PipelineNodeConfigurationImpl configuration;

    public SplitPipelineNodeProducer() {
        configuration = new PipelineNodeConfigurationImpl();
        configuration.addNodeInput(
                new PipelineNodeInputImpl(true, "input", Predicates.in(Arrays.asList(PropertyType.Vector2, PropertyType.Vector3, PropertyType.Color))));
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("x", PropertyType.Vector1));
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("y", PropertyType.Vector1));
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("z", PropertyType.Vector1));
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("w", PropertyType.Vector1));
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("Split");
    }

    @Override
    public PipelineNodeConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PipelineNode createNode(JSONObject data, final Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        return new OncePerFrameJobPipelineNode(configuration) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                Object input = inputFunctions.get("input").apply(pipelineRenderingContext);
                float x = 0f;
                float y = 0f;
                float z = 0f;
                float w = 0f;
                if (input instanceof Vector2) {
                    x = ((Vector2) input).x;
                    y = ((Vector2) input).y;
                } else if (input instanceof Vector3) {
                    x = ((Vector3) input).x;
                    y = ((Vector3) input).y;
                    z = ((Vector3) input).z;
                } else {
                    x = ((Color) input).r;
                    y = ((Color) input).g;
                    z = ((Color) input).b;
                    w = ((Color) input).a;
                }

                OutputValue<Float> outputX = outputValues.get("x");
                if (outputX != null)
                    outputX.setValue(x);
                OutputValue<Float> outputY = outputValues.get("y");
                if (outputY != null)
                    outputY.setValue(y);
                OutputValue<Float> outputZ = outputValues.get("z");
                if (outputZ != null)
                    outputZ.setValue(z);
                OutputValue<Float> outputW = outputValues.get("w");
                if (outputW != null)
                    outputW.setValue(w);
            }
        };
    }
}
