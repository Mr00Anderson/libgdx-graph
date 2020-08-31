package com.gempukku.libgdx.graph.shader.node.math.common;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.math.common.SmoothstepShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SmoothstepShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SmoothstepShaderNodeBuilder() {
        super(new SmoothstepShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput edge0Value = inputs.get("edge0");
        FieldOutput edge1Value = inputs.get("edge1");
        FieldOutput inputValue = inputs.get("input");
        ShaderFieldType resultType = inputValue.getFieldType();

        commonShaderBuilder.addMainLine("// Smoothstep node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = smoothstep(" + edge0Value.getRepresentation() + ", " + edge1Value.getRepresentation() + ", " + inputValue.getRepresentation() + ");");

        return Collections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
