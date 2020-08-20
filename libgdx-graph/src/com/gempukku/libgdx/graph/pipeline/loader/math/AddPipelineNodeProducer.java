package com.gempukku.libgdx.graph.pipeline.loader.math;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.config.math.MultiplyPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import org.json.simple.JSONObject;

import java.util.Map;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Float;

public class AddPipelineNodeProducer extends PipelineNodeProducerImpl {
    public AddPipelineNodeProducer() {
        super(new MultiplyPipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<?> aFunction = inputFields.get("inputA");
        final PipelineNode.FieldOutput<?> bFunction = inputFields.get("inputB");
        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected PipelineFieldType determineOutputType(String name, Map<String, FieldOutput<?>> inputFields) {
                FieldOutput<?> a = inputFields.get("inputA");
                FieldOutput<?> b = inputFields.get("inputB");
                PipelineFieldType aType = a.getPropertyType();
                PipelineFieldType bType = b.getPropertyType();
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
                    result = (float) aValue + (float) bValue;
                } else if (aValue instanceof Float) {
                    result = addFloat((float) aValue, bValue);
                } else if (bValue instanceof Float) {
                    result = addFloat((float) bValue, aValue);
                } else {
                    if (aValue.getClass() != bValue.getClass())
                        throw new IllegalArgumentException("Not matching types for add");
                    if (aValue instanceof Color) {
                        Color a = (Color) aValue;
                        result = a.cpy().add((Color) bValue);
                    } else if (aValue instanceof Vector2) {
                        Vector2 a = (Vector2) aValue;
                        result = add(a, (Vector2) bValue);
                    } else {
                        Vector3 a = (Vector3) aValue;
                        result = add(a, (Vector3) bValue);
                    }
                }

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(result);
            }
        };
    }

    private <T extends Vector<T>> T add(Vector<T> a, T b) {
        return a.cpy().add(b);
    }

    private Object addFloat(float aValue, Object bValue) {
        if (bValue instanceof Color) {
            return ((Color) bValue).cpy().add(aValue, aValue, aValue, aValue);
        } else if (bValue instanceof Vector2) {
            return ((Vector2) bValue).cpy().add(aValue, aValue);
        } else {
            return ((Vector3) bValue).cpy().add(aValue, aValue, aValue);
        }
    }
}
