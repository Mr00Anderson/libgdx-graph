package com.gempukku.libgdx.graph.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.RenderPipeline;
import static com.gempukku.libgdx.graph.graph.pipeline.PipelineFieldType.Stage;

public class UIRendererPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
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
