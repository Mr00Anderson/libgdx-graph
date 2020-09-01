package com.gempukku.libgdx.graph.shader.node.effect;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.effect.FresnelEffectShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class FresnelEffectShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public FresnelEffectShaderNodeBuilder() {
        super(new FresnelEffectShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput normalValue = inputs.get("normal");
        FieldOutput viewDirValue = inputs.get("viewDir");
        FieldOutput powerValue = inputs.get("power");

        commonShaderBuilder.addMainLine("// Fresnel effect node");
        String name = "result_" + nodeId;
        ShaderFieldType resultType = ShaderFieldType.Float;
        commonShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = pow((1.0 - clamp(dot(normalize(" + normalValue.getRepresentation() + "), normalize(" + viewDirValue.getRepresentation() + ")), 0.0, 1.0)), " + powerValue.getRepresentation() + ");");

        return Collections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }
}
