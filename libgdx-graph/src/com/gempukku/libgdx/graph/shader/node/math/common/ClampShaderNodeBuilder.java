package com.gempukku.libgdx.graph.shader.node.math.common;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.math.common.ClampShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ClampShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public ClampShaderNodeBuilder() {
        super(new ClampShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput inputValue = inputs.get("input");
        FieldOutput minValue = inputs.get("min");
        FieldOutput maxValue = inputs.get("max");
        ShaderFieldType resultType = determineOutputType(inputValue, minValue, maxValue);

        fragmentShaderBuilder.addMainLine("// Clamp node");
        String name = "result_" + nodeId;
        fragmentShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = clamp(" + inputValue.getRepresentation() + ", " + minValue.getRepresentation() + ", " + maxValue.getRepresentation() + ");");

        return Collections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }

    private ShaderFieldType determineOutputType(FieldOutput input, FieldOutput min, FieldOutput max) {
        ShaderFieldType inputType = input.getFieldType();
        ShaderFieldType minType = min.getFieldType();
        ShaderFieldType maxType = max.getFieldType();
        if (minType != maxType || !(minType == ShaderFieldType.Float || minType == inputType))
            throw new IllegalStateException("Invalid mix of input field types");
        return inputType;
    }
}
