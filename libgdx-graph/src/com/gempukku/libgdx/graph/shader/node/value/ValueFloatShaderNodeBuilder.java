package com.gempukku.libgdx.graph.shader.node.value;

import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.value.ValueFloatShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
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
    public Map<String, ? extends FieldOutput> buildNode(String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext) {
        float value = ((Number) data.get("v1")).floatValue();

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Float, format(value)));
    }

    private String format(float component) {
        return numberFormat.format(component);
    }
}
