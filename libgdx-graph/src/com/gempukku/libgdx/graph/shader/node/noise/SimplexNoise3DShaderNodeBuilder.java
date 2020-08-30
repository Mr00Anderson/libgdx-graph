package com.gempukku.libgdx.graph.shader.node.noise;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.config.noise.SimplexNoise3DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SimplexNoise3DShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SimplexNoise3DShaderNodeBuilder() {
        super(new SimplexNoise3DNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                                 CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput pointValue = inputs.get("point");
        FieldOutput scaleValue = inputs.get("scale");

        if (!commonShaderBuilder.containsFunction("simplexNoise3d")) {
            commonShaderBuilder.addFunction("simplexNoise3d", GLSLFragmentReader.getFragment("simplexNoise3d"));
        }

        commonShaderBuilder.addMainLine("// Simplex noise 3D node");
        String name = "result_" + nodeId;
        commonShaderBuilder.addMainLine("float " + name + " = simplexNoise3d(" + pointValue.getRepresentation() + " * " + scaleValue.getRepresentation() + ");");

        return Collections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
