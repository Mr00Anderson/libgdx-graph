package com.gempukku.libgdx.graph.shader.node.color;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.color.IntensityShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class IntensityShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public IntensityShaderNodeBuilder() {
        super(new IntensityShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput aValue = inputs.get("color");

        commonShaderBuilder.addMainLine("// Intensity node");
        String name = "result_" + nodeId;
        String value = aValue.getRepresentation();
        ShaderFieldType resultType = ShaderFieldType.Float;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = 0.3 * " + value + ".r + 0.59 * " + value + ".g + 0.11 * " + value + ".b;");

        return Collections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
