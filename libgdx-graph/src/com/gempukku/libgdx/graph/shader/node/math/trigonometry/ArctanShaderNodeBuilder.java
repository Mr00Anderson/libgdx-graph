package com.gempukku.libgdx.graph.shader.node.math.trigonometry;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.math.trigonometry.ArctanShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ArctanShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public ArctanShaderNodeBuilder() {
        super(new ArctanShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput inputValue = inputs.get("input");
        ShaderFieldType resultType = inputValue.getFieldType();

        fragmentShaderBuilder.addMainLine("// Arctangent node");
        String name = "result_" + nodeId;
        fragmentShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = atan(" + inputValue.getRepresentation() + ");");

        return Collections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
