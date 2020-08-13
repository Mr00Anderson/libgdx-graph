package com.gempukku.libgdx.graph.renderer.loader.provided;

import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.renderer.config.provided.TimePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import org.json.simple.JSONObject;

import java.util.Map;

public class TimePipelineNodeProducer extends PipelineNodeProducerImpl {
    public TimePipelineNodeProducer() {
        super(new TimePipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
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
