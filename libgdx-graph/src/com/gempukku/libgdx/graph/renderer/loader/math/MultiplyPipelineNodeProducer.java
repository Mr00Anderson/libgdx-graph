package com.gempukku.libgdx.graph.renderer.loader.math;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.renderer.config.math.MultiplyPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import java.util.Map;

public class MultiplyPipelineNodeProducer extends PipelineNodeProducerImpl {
    public MultiplyPipelineNodeProducer() {
        super(new MultiplyPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        final Function<PipelineRenderingContext, ?> aFunction = inputFunctions.get("inputA");
        final Function<PipelineRenderingContext, ?> bFunction = inputFunctions.get("inputB");
        return new OncePerFrameJobPipelineNode(configuration) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                Object aValue = aFunction.apply(pipelineRenderingContext);
                Object bValue = bFunction.apply(pipelineRenderingContext);

                Object result;
                if (aValue instanceof Float && bValue instanceof Float) {
                    result = (float) aValue * (float) bValue;
                } else if (aValue instanceof Float) {
                    result = mulFloat((float) aValue, bValue);
                } else if (bValue instanceof Float) {
                    result = mulFloat((float) bValue, aValue);
                } else {
                    if (aValue.getClass() != bValue.getClass())
                        throw new IllegalArgumentException("Not matching types for multiply");
                    if (aValue instanceof Color) {
                        Color a = (Color) aValue;
                        result = a.cpy().mul((Color) bValue);
                    } else if (aValue instanceof Vector2) {
                        Vector2 a = (Vector2) aValue;
                        result = mul(a, (Vector2) bValue);
                    } else {
                        Vector3 a = (Vector3) aValue;
                        result = mul(a, (Vector3) bValue);
                    }
                }

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(result);
            }
        };
    }

    private <T extends Vector<T>> T mul(Vector<T> a, T b) {
        return a.cpy().scl(b);
    }

    private Object mulFloat(float aValue, Object bValue) {
        if (bValue instanceof Color) {
            return ((Color) bValue).cpy().mul(aValue);
        } else {
            return ((Vector<?>) bValue).cpy().scl(aValue);
        }
    }
}
