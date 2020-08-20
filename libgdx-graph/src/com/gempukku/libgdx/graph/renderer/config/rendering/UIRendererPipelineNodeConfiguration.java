package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.RenderPipeline;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Stage;

public class UIRendererPipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public UIRendererPipelineNodeConfiguration() {
        super("UIRenderer", "UI renderer");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("stage", "Stage", true, Stage));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
