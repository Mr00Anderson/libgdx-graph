package com.gempukku.libgdx.graph.shader.node.value;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.value.ValueVector2ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ValueVector3ShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    private static NumberFormat numberFormat = new DecimalFormat("0.0000");

    public ValueVector3ShaderNodeBuilder() {
        super(new ValueVector2ShaderNodeConfiguration());
    }

    @Override
    public Map<String, FieldOutput> buildNode(String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        float v1 = ((Number) data.get("v1")).floatValue();
        float v2 = ((Number) data.get("v2")).floatValue();
        float v3 = ((Number) data.get("v3")).floatValue();

        final String variable = "value_" + nodeId;
        fragmentShaderBuilder.addMainLine("vec3 " + variable + " = vec3(" + format(v1) + ", " + format(v2) + ", " + format(v3) + ");");

        FieldOutput valueOutput = new FieldOutput() {
            @Override
            public ShaderFieldType getFieldType() {
                return ShaderFieldType.Vector3;
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
