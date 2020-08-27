package com.gempukku.libgdx.graph.shader.node.noise;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.noise.SimplexNoise2DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SimplexNoise2DShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public SimplexNoise2DShaderNodeBuilder() {
        super(new SimplexNoise2DNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput scaleValue = inputs.get("scale");

        if (!fragmentShaderBuilder.containsFunction("simplexNoise2d")) {
            fragmentShaderBuilder.addFunction("simplexNoise2d", GLSLFragmentReader.getFragment("simplexNoise2d"));
        }

        fragmentShaderBuilder.addMainLine("// Simplex noise 2D node");
        String name = "result_" + nodeId;
        if (uvValue.getFieldType() == ShaderFieldType.Vector2) {
            fragmentShaderBuilder.addMainLine("float " + name + " = simplexNoise2d(" + uvValue.getRepresentation() + " * " + scaleValue.getRepresentation() + ");");
        } else {
            fragmentShaderBuilder.addMainLine("float " + name + " = simplexNoise2d(vec2(" + uvValue.getRepresentation() + ", 0.0) * " + scaleValue.getRepresentation() + ");");
        }

        return Collections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
