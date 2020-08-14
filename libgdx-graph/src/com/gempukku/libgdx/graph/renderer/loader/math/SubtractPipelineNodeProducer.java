package com.gempukku.libgdx.graph.renderer.loader.math;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.config.math.MultiplyPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import org.json.simple.JSONObject;

import java.util.Map;

import static com.gempukku.libgdx.graph.renderer.PropertyType.Float;

public class SubtractPipelineNodeProducer extends PipelineNodeProducerImpl {
    public SubtractPipelineNodeProducer() {
        super(new MultiplyPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<?> aFunction = inputFields.get("inputA");
        final PipelineNode.FieldOutput<?> bFunction = inputFields.get("inputB");
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected PropertyType determineOutputType(String name, Map<String, FieldOutput<?>> inputFields) {
                FieldOutput<?> a = inputFields.get("inputA");
                FieldOutput<?> b = inputFields.get("inputB");
                PropertyType aType = a.getPropertyType();
                PropertyType bType = b.getPropertyType();
                if (aType == Float)
                    return bType;
                if (bType == Float)
                    return aType;
                if (aType != bType)
                    throw new IllegalStateException("Invalid mix of input field types");
                return aType;
            }

            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                Object aValue = aFunction.getValue(pipelineRenderingContext);
                Object bValue = bFunction.getValue(pipelineRenderingContext);

                Object result;
                if (aValue instanceof Float && bValue instanceof Float) {
                    result = (float) aValue - (float) bValue;
                } else if (aValue instanceof Float) {
                    throw new IllegalArgumentException("Not matching types for subtract");
                } else if (bValue instanceof Float) {
                    result = subtractFloat(aValue, (float) bValue);
                } else {
                    if (aValue.getClass() != bValue.getClass())
                        throw new IllegalArgumentException("Not matching types for subtract");
                    if (aValue instanceof Color) {
                        Color a = (Color) aValue;
                        result = a.cpy().sub((Color) bValue);
                    } else if (aValue instanceof Vector2) {
                        Vector2 a = (Vector2) aValue;
                        result = sub(a, (Vector2) bValue);
                    } else {
                        Vector3 a = (Vector3) aValue;
                        result = sub(a, (Vector3) bValue);
                    }
                }

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(result);
            }
        };
    }

    private <T extends Vector<T>> T sub(Vector<T> a, T b) {
        return a.cpy().sub(b);
    }

    private Object subtractFloat(Object aValue, float bValue) {
        if (aValue instanceof Color) {
            return ((Color) aValue).cpy().sub(bValue, bValue, bValue, bValue);
        } else if (aValue instanceof Vector2) {
            return ((Vector2) aValue).cpy().sub(bValue, bValue);
        } else {
            return ((Vector3) aValue).cpy().sub(bValue, bValue, bValue);
        }
    }
}
