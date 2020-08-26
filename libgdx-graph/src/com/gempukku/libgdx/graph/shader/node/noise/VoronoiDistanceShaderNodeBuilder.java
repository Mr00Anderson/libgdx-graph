package com.gempukku.libgdx.graph.shader.node.noise;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.noise.VoronoiDistanceNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class VoronoiDistanceShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public VoronoiDistanceShaderNodeBuilder() {
        super(new VoronoiDistanceNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput scaleValue = inputs.get("scale");

        if (!fragmentShaderBuilder.containsFunction("voronoiDistance")) {
            fragmentShaderBuilder.addFunction("voronoiDistance", GLSLFragmentReader.getFragment("voronoiDistance"));
        }

        fragmentShaderBuilder.addMainLine("// Voronoi distance node");
        String name = "result_" + nodeId;
        if (uvValue.getFieldType() == ShaderFieldType.Vector2) {
            fragmentShaderBuilder.addMainLine("float " + name + " = voronoiDistance(" + uvValue.getRepresentation() + " * " + scaleValue.getRepresentation() + ");");
        } else {
            fragmentShaderBuilder.addMainLine("float " + name + " = voronoiDistance(vec2(" + uvValue.getRepresentation() + ", 0.0) * " + scaleValue.getRepresentation() + ");");
        }

        return Collections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
