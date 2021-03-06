package com.gempukku.libgdx.graph.shader.node.value;

import com.badlogic.gdx.graphics.Color;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.value.ValueColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ValueColorShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    private static NumberFormat numberFormat = new DecimalFormat("0.0######");

    public ValueColorShaderNodeBuilder() {
        super(new ValueColorShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final Color color = Color.valueOf((String) data.get("color"));

        String value = "vec4(" + format(color.r) + ", " + format(color.g) + ", " + format(color.b) + ", " + format(color.a) + ")";
        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Color, value));
    }

    private String format(float component) {
        return numberFormat.format(component);
    }
}
