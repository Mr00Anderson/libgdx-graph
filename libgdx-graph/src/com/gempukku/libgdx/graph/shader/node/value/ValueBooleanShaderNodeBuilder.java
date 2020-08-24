package com.gempukku.libgdx.graph.shader.node.value;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.value.ValueBooleanShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ValueBooleanShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public ValueBooleanShaderNodeBuilder() {
        super(new ValueBooleanShaderNodeConfiguration());
    }

    @Override
    public Map<String, FieldOutput> buildNode(String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        boolean value = (Boolean) data.get("value");

        final String variable = "value_" + nodeId;
        fragmentShaderBuilder.addMainLine("bool " + variable + " = " + value + ";");

        FieldOutput valueOutput = new FieldOutput() {
            @Override
            public ShaderFieldType getFieldType() {
                return ShaderFieldType.Boolean;
            }

            @Override
            public String getRepresentation() {
                return variable;
            }
        };

        return Collections.singletonMap("value", valueOutput);
    }
}
