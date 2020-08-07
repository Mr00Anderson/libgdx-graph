package com.gempukku.libgdx.graph.renderer.loader.rendering.producer;

import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.RenderPipeline;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducer;
import com.google.common.base.Function;
import com.google.common.base.Predicates;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.Map;

public class UIRendererPipelineNodeProducer implements PipelineNodeProducer {
    private PipelineNodeConfigurationImpl configuration;

    public UIRendererPipelineNodeProducer() {
        configuration = new PipelineNodeConfigurationImpl();
        configuration.addNodeInput(
                new PipelineNodeInputImpl(false, "stage",
                        Predicates.equalTo(PropertyType.Stage)));
        configuration.addNodeInput(
                new PipelineNodeInputImpl(true, "input",
                        Predicates.equalTo(PropertyType.RenderPipeline)));
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("output", PropertyType.RenderPipeline));
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("UIRenderer");
    }

    @Override
    public PipelineNodeConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PipelineNode createNode(JSONObject data, final Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        Function<PipelineRenderingContext, Stage> stageInput = (Function<PipelineRenderingContext, Stage>) inputFunctions.get("stage");
        final Function<PipelineRenderingContext, RenderPipeline> renderPipelineInput = (Function<PipelineRenderingContext, RenderPipeline>) inputFunctions.get("input");
        if (stageInput == null)
            stageInput = new Function<PipelineRenderingContext, Stage>() {
                @Override
                public Stage apply(@Nullable PipelineRenderingContext pipelineRenderingContext) {
                    return null;
                }
            };
        final Function<PipelineRenderingContext, Stage> finalStageInput = stageInput;
        return new OncePerFrameJobPipelineNode(configuration) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                RenderPipeline renderPipeline = renderPipelineInput.apply(pipelineRenderingContext);
                Stage stage = finalStageInput.apply(pipelineRenderingContext);
                if (stage != null) {
                    FrameBuffer currentBuffer = renderPipeline.getCurrentBuffer();
                    int width = currentBuffer.getWidth();
                    int height = currentBuffer.getHeight();
                    int screenWidth = stage.getViewport().getScreenWidth();
                    int screenHeight = stage.getViewport().getScreenHeight();
                    if (screenWidth != width || screenHeight != height)
                        stage.getViewport().update(width, height, true);
                    currentBuffer.begin();
                    stage.draw();
                    currentBuffer.end();
                }
                OutputValue output = outputValues.get("output");
                if (output != null)
                    output.setValue(renderPipeline);
            }
        };
    }
}
