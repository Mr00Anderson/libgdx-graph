package com.gempukku.libgdx.graph.renderer.loader.rendering.producer;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.config.rendering.PipelineRendererNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import java.util.Map;

public class PipelineRendererNodeProducer extends PipelineNodeProducerImpl {
    public PipelineRendererNodeProducer() {
        super(new PipelineRendererNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        final Function<PipelineRenderingContext, RenderPipeline> renderPipelineInput = (Function<PipelineRenderingContext, RenderPipeline>) inputFunctions.get("input");
        final Function<PipelineRenderingContext, RenderPipeline> otherPipelineInput = (Function<PipelineRenderingContext, RenderPipeline>) inputFunctions.get("pipeline");
        final Function<PipelineRenderingContext, Vector2> positionInput = (Function<PipelineRenderingContext, Vector2>) inputFunctions.get("position");
        final Function<PipelineRenderingContext, Vector2> sizeInput = (Function<PipelineRenderingContext, Vector2>) inputFunctions.get("size");

        return new OncePerFrameJobPipelineNode(getConfiguration()) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                RenderPipeline canvasPipeline = renderPipelineInput.apply(pipelineRenderingContext);
                RenderPipeline paintPipeline = otherPipelineInput.apply(pipelineRenderingContext);

                Vector2 position = positionInput.apply(pipelineRenderingContext);
                Vector2 size = sizeInput.apply(pipelineRenderingContext);

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
