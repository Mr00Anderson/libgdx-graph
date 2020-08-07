package com.gempukku.libgdx.graph.renderer.loader.rendering.provided;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.renderer.loader.node.OncePerFrameJobPipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducer;
import com.google.common.base.Function;
import org.json.simple.JSONObject;

import java.util.Map;

public class ScreenSizePipelineNodeProducer implements PipelineNodeProducer {
    private PipelineNodeConfigurationImpl configuration;

    public ScreenSizePipelineNodeProducer() {
        configuration = new PipelineNodeConfigurationImpl();
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("size", PropertyType.Vector2));
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("width", PropertyType.Vector1));
        configuration.addNodeOutput(
                new PipelineNodeOutputImpl("height", PropertyType.Vector1));
    }

    @Override
    public boolean supportsType(String type) {
        return type.equals("ScreenSize");
    }

    @Override
    public PipelineNodeConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, Function<PipelineRenderingContext, ?>> inputFunctions) {
        return new OncePerFrameJobPipelineNode(configuration) {
            @Override
            protected void executeJob(PipelineRenderingContext pipelineRenderingContext, Map<String, ? extends OutputValue> outputValues) {
                int width = Gdx.graphics.getWidth();
                int height = Gdx.graphics.getHeight();
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
