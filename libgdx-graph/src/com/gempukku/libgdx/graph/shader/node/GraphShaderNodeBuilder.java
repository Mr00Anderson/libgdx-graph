package com.gempukku.libgdx.graph.shader.node;

import com.gempukku.libgdx.graph.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import org.json.simple.JSONObject;

import java.util.Map;
import java.util.Set;

public interface GraphShaderNodeBuilder {
    String getType();

    NodeConfiguration<ShaderFieldType> getConfiguration(JSONObject data);

    Map<String, FieldOutput> buildNode(String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                       VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder);

    interface FieldOutput {
        ShaderFieldType getFieldType();

        String getRepresentation();
    }
}
