package com.gempukku.libgdx.graph.shader.node.math.common;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.math.common.ClampShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ClampShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ClampShaderNodeBuilder() {
        super(new ClampShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput inputValue = inputs.get("input");
        FieldOutput minValue = inputs.get("min");
        FieldOutput maxValue = inputs.get("max");
        ShaderFieldType resultType = determineOutputType(inputValue, minValue, maxValue);

        commonShaderBuilder.addMainLine("// Clamp node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = clamp(" + inputValue.getRepresentation() + ", " + minValue.getRepresentation() + ", " + maxValue.getRepresentation() + ");");

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
