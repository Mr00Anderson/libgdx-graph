package com.gempukku.libgdx.graph.shader.node.part;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.part.MergeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MergeShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public MergeShaderNodeBuilder() {
        super(new MergeShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                                 CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput xValue = inputs.get("x");
        FieldOutput yValue = inputs.get("y");
        FieldOutput zValue = inputs.get("z");
        FieldOutput wValue = inputs.get("w");

        String x = xValue != null ? xValue.getRepresentation() : "0.0";
        String y = yValue != null ? yValue.getRepresentation() : "0.0";
        String z = zValue != null ? zValue.getRepresentation() : "0.0";
        String w = wValue != null ? wValue.getRepresentation() : "0.0";

        commonShaderBuilder.addMainLine("// Merge Node");

        Map<String, DefaultFieldOutput> result = new HashMap<>();
        if (producedOutputs.contains("v2")) {
            String name = "v2_" + nodeId;
            commonShaderBuilder.addMainLine("vec2 " + name + " = vec2(" + x + ", " + y + ");");
            result.put("v2", new DefaultFieldOutput(ShaderFieldType.Vector2, name));
        }
        if (producedOutputs.contains("v3")) {
            String name = "v3_" + nodeId;
            commonShaderBuilder.addMainLine("vec3 " + name + " = vec3(" + x + ", " + y + ", " + z + ");");
            result.put("v3", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("color")) {
            String name = "color_" + nodeId;
            commonShaderBuilder.addMainLine("vec4 " + name + " = vec4(" + x + ", " + y + ", " + z + ", " + w + ");");
            result.put("color", new DefaultFieldOutput(ShaderFieldType.Color, name));
        }
        return result;
    }
}
