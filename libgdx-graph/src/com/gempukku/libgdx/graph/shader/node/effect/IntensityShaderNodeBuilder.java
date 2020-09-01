package com.gempukku.libgdx.graph.shader.node.effect;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.effect.IntensityShaderNodeConfiguration;
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
        ShaderFieldType resultType = ShaderFieldType.Float;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = dot(" + aValue.getRepresentation() + ".rgb, vec3(0.2126729, 0.7151522, 0.0721750));");

        return Collections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
