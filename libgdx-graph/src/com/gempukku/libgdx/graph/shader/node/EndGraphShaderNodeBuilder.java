package com.gempukku.libgdx.graph.shader.node;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.EndShaderNodeConfiguration;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class EndGraphShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public EndGraphShaderNodeBuilder() {
        super(new EndShaderNodeConfiguration());
    }

    @Override
    public Map<String, FieldOutput> buildNode(String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                              VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        FieldOutput alphaField = inputs.get("alpha");
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";
        FieldOutput alphaClipField = inputs.get("alphaClip");
        String alphaClip = (alphaClipField != null) ? alphaClipField.getRepresentation() : "0.0";
        fragmentShaderBuilder.addMainLine("if (" + alpha + " < " + alphaClip + ")");
        fragmentShaderBuilder.addMainLine("  discard;");

        FieldOutput colorField = inputs.get("color");
        String color;
        if (colorField == null) {
            color = "vec4(1.0, 1.0, 1.0, " + alpha + ")";
        } else if (colorField.getFieldType() == ShaderFieldType.Color) {
            color = "vec4(" + colorField.getRepresentation() + ".rgb, " + alpha + ")";
        } else {
            color = "vec4(" + colorField.getRepresentation() + ", " + alpha + ")";
        }
        fragmentShaderBuilder.addMainLine("gl_FragColor = " + color + ";");

        return Collections.emptyMap();
    }
}
