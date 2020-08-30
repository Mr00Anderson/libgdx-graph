package com.gempukku.libgdx.graph.shader.node.noise;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.config.noise.VoronoiDistanceNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class VoronoiDistanceShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public VoronoiDistanceShaderNodeBuilder() {
        super(new VoronoiDistanceNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                                 CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput scaleValue = inputs.get("scale");

        if (!commonShaderBuilder.containsFunction("voronoiDistance")) {
            commonShaderBuilder.addFunction("voronoiDistance", GLSLFragmentReader.getFragment("voronoiDistance"));
        }

        commonShaderBuilder.addMainLine("// Voronoi distance node");
        String name = "result_" + nodeId;
        if (uvValue.getFieldType() == ShaderFieldType.Vector2) {
            commonShaderBuilder.addMainLine("float " + name + " = voronoiDistance(" + uvValue.getRepresentation() + " * " + scaleValue.getRepresentation() + ");");
        } else {
            commonShaderBuilder.addMainLine("float " + name + " = voronoiDistance(vec2(" + uvValue.getRepresentation() + ", 0.0) * " + scaleValue.getRepresentation() + ");");
        }

        return Collections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
