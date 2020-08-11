package com.gempukku.libgdx.graph.data;

import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInput;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutput;

public interface GraphNode {
    boolean isInputField(String fieldId);

    PipelineNodeInput getInput(String fieldId);

    PipelineNodeOutput getOutput(String fieldId);
}
