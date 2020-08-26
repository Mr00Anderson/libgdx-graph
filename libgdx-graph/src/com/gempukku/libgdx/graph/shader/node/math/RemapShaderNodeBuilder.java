package com.gempukku.libgdx.graph.shader.node.math;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.math.RemapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class RemapShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public RemapShaderNodeBuilder() {
        super(new RemapShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput inputValue = inputs.get("input");
        FieldOutput fromValue = inputs.get("from");
        FieldOutput toValue = inputs.get("to");
        ShaderFieldType resultType = inputValue.getFieldType();

        if (fragmentShaderBuilder.containsFunction("remap")) {
            fragmentShaderBuilder.addFunction("remap", resultType.getShaderType() + " remap(" + resultType.getShaderType() + " input, vec2 from, vec2 to) {\n" +
                    "  return to.x + (input - from.x) * (to.y - to.x) / (from.y - from.x);\n" +
                    "}\n");
        }
        fragmentShaderBuilder.addMainLine("// Remap node");
        String name = "result_" + nodeId;
        fragmentShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = remap(" + inputValue.getRepresentation() + ", " + fromValue.getRepresentation() + ", " + toValue.getRepresentation() + ");");

        return Collections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
