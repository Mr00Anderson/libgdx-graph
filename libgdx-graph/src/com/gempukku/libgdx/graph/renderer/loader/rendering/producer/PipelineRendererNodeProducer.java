package com.gempukku.libgdx.graph.renderer.loader.rendering.producer;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.config.rendering.PipelineRendererNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import org.json.simple.JSONObject;

import java.util.Map;

public class PipelineRendererNodeProducer extends PipelineNodeProducerImpl {
    public PipelineRendererNodeProducer() {
        super(new PipelineRendererNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        final PipelineNode.FieldOutput<RenderPipeline> renderPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("input");
        final PipelineNode.FieldOutput<RenderPipeline> otherPipelineInput = (PipelineNode.FieldOutput<RenderPipeline>) inputFields.get("pipeline");
        final PipelineNode.FieldOutput<Vector2> positionInput = (PipelineNode.FieldOutput<Vector2>) inputFields.get("position");
        final PipelineNode.FieldOutput<Vector2> sizeInput = (PipelineNode.FieldOutput<Vector2>) inputFields.get("size");

        return new OncePerFrameJobPipelineNode(configuration, inputFields) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                RenderPipeline canvasPipeline = renderPipelineInput.getValue(pipelineRenderingContext);
                RenderPipeline paintPipeline = otherPipelineInput.getValue(pipelineRenderingContext);

                Vector2 position = positionInput.getValue(pipelineRenderingContext);
                Vector2 size = sizeInput.getValue(pipelineRenderingContext);

                FrameBuffer canvasBuffer = canvasPipeline.getCurrentBuffer();
                FrameBuffer paintBuffer = paintPipeline.getCurrentBuffer();
                canvasPipeline.getBufferCopyHelper().copy(paintBuffer, canvasBuffer, position.x, position.y, size.x, size.y);

                paintPipeline.returnFrameBuffer(paintBuffer);

                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(canvasPipeline);
            }
        };
    }
}
