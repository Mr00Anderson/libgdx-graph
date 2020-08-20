package com.gempukku.libgdx.graph.pipeline.loader.part;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.pipeline.config.part.MergePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.loader.FloatFieldOutput;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducerImpl;
import org.json.simple.JSONObject;

import java.util.Map;

public class MergePipelineNodeProducer extends PipelineNodeProducerImpl {
    public MergePipelineNodeProducer() {
        super(new MergePipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        PipelineNode.FieldOutput<Float> x = (PipelineNode.FieldOutput<Float>) inputFields.get("x");
        PipelineNode.FieldOutput<Float> y = (PipelineNode.FieldOutput<Float>) inputFields.get("y");
        PipelineNode.FieldOutput<Float> z = (PipelineNode.FieldOutput<Float>) inputFields.get("z");
        PipelineNode.FieldOutput<Float> w = (PipelineNode.FieldOutput<Float>) inputFields.get("w");
        if (x == null)
            x = new FloatFieldOutput(0f);
        if (y == null)
            y = new FloatFieldOutput(0f);
        if (z == null)
            z = new FloatFieldOutput(0f);
        if (w == null)
            w = new FloatFieldOutput(0f);
        final PipelineNode.FieldOutput<Float> finalX = x;
        final PipelineNode.FieldOutput<Float> finalY = y;
        final PipelineNode.FieldOutput<Float> finalZ = z;
        final PipelineNode.FieldOutput<Float> finalW = w;

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                float xValue = finalX.getValue(pipelineRenderingContext);
                float yValue = finalY.getValue(pipelineRenderingContext);
                float zValue = finalZ.getValue(pipelineRenderingContext);
                float wValue = finalW.getValue(pipelineRenderingContext);

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
