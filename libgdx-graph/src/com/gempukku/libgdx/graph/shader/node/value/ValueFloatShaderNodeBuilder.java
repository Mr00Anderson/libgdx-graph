package com.gempukku.libgdx.graph.shader.node.value;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.value.ValueFloatShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ValueFloatShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    private static NumberFormat numberFormat = new DecimalFormat("0.0000");

    public ValueFloatShaderNodeBuilder() {
        super(new ValueFloatShaderNodeConfiguration());
    }

    @Override
    public Map<String, FieldOutput> buildNode(String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        float value = ((Number) data.get("value")).floatValue();

        final String variable = "value_" + nodeId;
        fragmentShaderBuilder.addMainLine("float " + variable + " = " + format(value) + ";");

        FieldOutput valueOutput = new FieldOutput() {
            @Override
            public ShaderFieldType getFieldType() {
                return ShaderFieldType.Float;
            }

            @Override
            public String getRepresentation() {
                return variable;
            }
        };

        return Collections.singletonMap("value", valueOutput);
    }

    private String format(float component) {
        return numberFormat.format(component);
    }
}
