package com.gempukku.libgdx.graph.renderer.loader.provided;

import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.renderer.config.provided.RenderSizePipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducerImpl;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import java.util.Map;

public class RenderSizePipelineNodeProducer extends PipelineNodeProducerImpl {
    public RenderSizePipelineNodeProducer() {
        super(new RenderSizePipelineNodeConfiguration());
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        return new OncePerFrameJobPipelineNode(configuration) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                int width = pipelineRenderingContext.getRenderWidth();
                int height = pipelineRenderingContext.getRenderHeight();
                OutputValue<Vector2> size = outputValues.get("size");
                if (size != null)
                    size.setValue(new Vector2(width, height));
                OutputValue<Float> widthValue = outputValues.get("width");
                if (widthValue != null)
                    widthValue.setValue(1f * width);
                OutputValue<Float> heightValue = outputValues.get("height");
                if (heightValue != null)
                    heightValue.setValue(1f * height);
            }
        };
    }
}
