package com.gempukku.libgdx.graph.shader.node.math.value;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.math.value.RemapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class RemapShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public RemapShaderNodeBuilder() {
        super(new RemapShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput inputValue = inputs.get("input");
        FieldOutput fromValue = inputs.get("from");
        FieldOutput toValue = inputs.get("to");
        ShaderFieldType resultType = inputValue.getFieldType();

        String functionName = "remap_" + resultType.getShaderType();

        if (!commonShaderBuilder.containsFunction(functionName)) {
            commonShaderBuilder.addFunction(functionName, resultType.getShaderType() + " " + functionName + "(" + resultType.getShaderType() + " value, vec2 from, vec2 to) {\n" +
                    "  return to.x + (value - from.x) * (to.y - to.x) / (from.y - from.x);\n" +
                    "}\n");
        }
        commonShaderBuilder.addMainLine("// Remap node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = " + functionName + "(" + inputValue.getRepresentation() + ", " + fromValue.getRepresentation() + ", " + toValue.getRepresentation() + ");");

        return Collections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
