package com.gempukku.libgdx.graph.shader.node.noise;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.noise.SimplexNoiseNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SimplexNoiseShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public SimplexNoiseShaderNodeBuilder() {
        super(new SimplexNoiseNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput scaleValue = inputs.get("scale");

        if (!fragmentShaderBuilder.containsFunction("simplexNoise")) {
            fragmentShaderBuilder.addFunction("simplexNoise", GLSLFragmentReader.getFragment("simplexNoise"));
        }

        fragmentShaderBuilder.addMainLine("// Simplex noise node");
        String name = "result_" + nodeId;
        fragmentShaderBuilder.addMainLine("float  " + name + " = simplexNoise(" + uvValue.getRepresentation() + " * " + scaleValue.getRepresentation() + ");");

        return Collections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
