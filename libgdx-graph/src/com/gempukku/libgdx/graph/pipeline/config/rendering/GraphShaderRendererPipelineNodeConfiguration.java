package com.gempukku.libgdx.graph.pipeline.config.rendering;

import com.gempukku.libgdx.graph.NodeConfigurationImpl;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.pipeline.loader.node.GraphNodeOutputImpl;

import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.Camera;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.GraphLights;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.GraphModels;
import static com.gempukku.libgdx.graph.pipeline.PipelineFieldType.RenderPipeline;

public class GraphShaderRendererPipelineNodeConfiguration extends NodeConfigurationImpl<PipelineFieldType> {
    public GraphShaderRendererPipelineNodeConfiguration() {
        super("GraphShaderRenderer", "Graph Shaders", "Pipeline");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("models", "Graph Models", true, GraphModels));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("camera", "Camera", true, Camera));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("lights", "Graph Lights", GraphLights));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
