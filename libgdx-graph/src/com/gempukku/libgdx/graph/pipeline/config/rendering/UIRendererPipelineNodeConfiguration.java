package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.RenderPipeline;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Stage;

public class UIRendererPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public UIRendererPipelineNodeConfiguration() {
        super("UIRenderer", "UI renderer", "Pipeline");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("stage", "Stage", true, Stage));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
