package com.gempukku.libgdx.graph.shader.node.value;

import com.badlogic.gdx.graphics.Color;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.value.ValueColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ValueColorShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    private static NumberFormat numberFormat = new DecimalFormat("0.0000");

    public ValueColorShaderNodeBuilder() {
        super(new ValueColorShaderNodeConfiguration());
    }

    @Override
    public Map<String, FieldOutput> buildNode(String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        final Color color = Color.valueOf((String) data.get("color"));

        final String variable = "value_" + nodeId;
        fragmentShaderBuilder.addMainLine("vec4 " + variable + " = vec4(" + format(color.r) + ", " + format(color.g) + ", " + format(color.b) + ", " + format(color.a) + ");");

        FieldOutput valueOutput = new FieldOutput() {
            @Override
            public ShaderFieldType getFieldType() {
                return ShaderFieldType.Color;
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
