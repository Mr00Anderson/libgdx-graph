package com.gempukku.libgdx.graph.shader.node.lighting;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.lighting.AmbientLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class AmbientLightShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public AmbientLightShaderNodeBuilder() {
        super(new AmbientLightShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        commonShaderBuilder.addUniformVariable("u_ambientLight", "vec3", true, UniformSetters.ambientLight);
        return Collections.singletonMap("ambient", new DefaultFieldOutput(ShaderFieldType.Vector3, "u_ambientLight"));
    }
}
