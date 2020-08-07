package com.gempukku.libgdx.graph.renderer.loader.provided;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducer;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import java.util.Map;

public class TimePipelineNodeProducer implements PipelineNodeProducer {
    private PipelineNodeConfigurationImpl configuration;

    public TimePipelineNodeProducer() {
        configuration = new PipelineNodeConfigurationImpl();
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("time", PropertyType.Vector1));
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("sinTime", PropertyType.Vector1));
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("cosTime", PropertyType.Vector1));
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("deltaTime", PropertyType.Vector1));
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("Time");
    }

    @Override
    public PipelineNodeConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        return new OncePerFrameJobPipelineNode(configuration) {
            private float timeCumulative = -1;
            private float delta;

            @Override
            public void startFrame(float delta) {
                this.delta = delta;
                if (timeCumulative > -1)
                    timeCumulative += delta;
                else
                    timeCumulative = 0;
            }

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                OutputValue<Float> time = outputValues.get("time");
                if (time != null)
                    time.setValue(timeCumulative);
                OutputValue<Float> sinTime = outputValues.get("sinTime");
                if (sinTime != null)
                    sinTime.setValue(MathUtils.sin(timeCumulative));
                OutputValue<Float> cosTime = outputValues.get("cosTime");
                if (cosTime != null)
                    cosTime.setValue(MathUtils.cos(timeCumulative));
                OutputValue<Float> deltaTime = outputValues.get("deltaTime");
                if (deltaTime != null)
                    deltaTime.setValue(delta);
            }
        };
    }
}
