package com.gempukku.libgdx.graph.renderer.loader.part;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.renderer.config.part.MergePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.Map;

public class MergePipelineNodeProducer extends PipelineNodeProducerImpl {
    public MergePipelineNodeProducer() {
        super(new MergePipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, final Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        Function<PipelineRenderingContext, Float> x = (Function<PipelineRenderingContext, Float>) inputFunctions.get("x");
        Function<PipelineRenderingContext, Float> y = (Function<PipelineRenderingContext, Float>) inputFunctions.get("y");
        Function<PipelineRenderingContext, Float> z = (Function<PipelineRenderingContext, Float>) inputFunctions.get("z");
        Function<PipelineRenderingContext, Float> w = (Function<PipelineRenderingContext, Float>) inputFunctions.get("w");
        if (x == null)
            x = new Function<PipelineRenderingContext, Float>() {
                @Override
                public Float apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                    return 0f;
                }
            };
        if (y == null)
            y = new Function<PipelineRenderingContext, Float>() {
                @Override
                public Float apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                    return 0f;
                }
            };
        if (z == null)
            z = new Function<PipelineRenderingContext, Float>() {
                @Override
                public Float apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                    return 0f;
                }
            };
        if (w == null)
            w = new Function<PipelineRenderingContext, Float>() {
                @Override
                public Float apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                    return 0f;
                }
            };
        final Function<PipelineRenderingContext, Float> finalX = x;
        final Function<PipelineRenderingContext, Float> finalY = y;
        final Function<PipelineRenderingContext, Float> finalZ = z;
        final Function<PipelineRenderingContext, Float> finalW = w;

        return new OncePerFrameJobPipelineNode(getConfiguration()) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                float xValue = finalX.apply(pipelineRenderingContext);
                float yValue = finalY.apply(pipelineRenderingContext);
                float zValue = finalZ.apply(pipelineRenderingContext);
                float wValue = finalW.apply(pipelineRenderingContext);

                OutputValue<Vector2> v2 = outputValues.get("v2");
                if (v2 != null)
                    v2.setValue(new Vector2(xValue, yValue));
                OutputValue<Vector3> v3 = outputValues.get("v3");
                if (v3 != null)
                    v3.setValue(new Vector3(xValue, yValue, zValue));
                OutputValue<Color> color = outputValues.get("color");
                if (color != null)
                    color.setValue(new Color(xValue, yValue, zValue, wValue));
            }
        };
    }
}
