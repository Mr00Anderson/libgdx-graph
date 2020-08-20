package com.gempukku.libgdx.graph.renderer.config.rendering;

import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeInputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.GraphNodeOutputImpl;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfigurationImpl;

import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Camera;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Lights;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.Models;
import static com.gempukku.libgdx.graph.renderer.PipelineFieldType.RenderPipeline;

public class GraphShaderRendererPipelineNodeConfiguration extends PipelineNodeConfigurationImpl<PipelineFieldType> {
    public GraphShaderRendererPipelineNodeConfiguration() {
        super("GraphShaderRenderer", "Graph Shaders");
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("models", "Models", true, Models));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("camera", "Camera", true, Camera));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("lights", "Lights", Lights));
        addNodeInput(
                new GraphNodeInputImpl<PipelineFieldType>("input", "Input", true, true, RenderPipeline));
        addNodeOutput(
                new GraphNodeOutputImpl<PipelineFieldType>("output", "Output", true, RenderPipeline));
    }
}
