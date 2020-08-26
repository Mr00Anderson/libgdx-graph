package com.gempukku.libgdx.graph.shader.node.part;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.part.MergeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MergeShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public MergeShaderNodeBuilder() {
        super(new MergeShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput xValue = inputs.get("x");
        FieldOutput yValue = inputs.get("y");
        FieldOutput zValue = inputs.get("z");
        FieldOutput wValue = inputs.get("w");

        String x = xValue != null ? xValue.getRepresentation() : "0.0";
        String y = yValue != null ? yValue.getRepresentation() : "0.0";
        String z = zValue != null ? zValue.getRepresentation() : "0.0";
        String w = wValue != null ? wValue.getRepresentation() : "0.0";

        fragmentShaderBuilder.addMainLine("// Merge Node");

        Map<String, DefaultFieldOutput> result = new HashMap<>();
        if (producedOutputs.contains("v2")) {
            String name = "v2_" + nodeId;
            fragmentShaderBuilder.addMainLine("vec2 " + name + " = vec2(" + x + ", " + y + ");");
            result.put("v2", new DefaultFieldOutput(ShaderFieldType.Vector2, name));
        }
        if (producedOutputs.contains("v3")) {
            String name = "v3_" + nodeId;
            fragmentShaderBuilder.addMainLine("vec3 " + name + " = vec3(" + x + ", " + y + ", " + z + ");");
            result.put("v3", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("color")) {
            String name = "color_" + nodeId;
            fragmentShaderBuilder.addMainLine("vec4 " + name + " = vec4(" + x + ", " + y + ", " + z + ", " + w + ");");
            result.put("color", new DefaultFieldOutput(ShaderFieldType.Color, name));
        }
        return result;
    }
}
