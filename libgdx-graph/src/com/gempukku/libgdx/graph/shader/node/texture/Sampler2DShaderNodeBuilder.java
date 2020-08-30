package com.gempukku.libgdx.graph.shader.node.texture;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.texture.Sampler2DShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Sampler2DShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public Sampler2DShaderNodeBuilder() {
        super(new Sampler2DShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        throw new UnsupportedOperationException("Sampling of textures is not available in vertex shader in OpenGL ES");
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                                 CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput textureValue = inputs.get("texture");
        FieldOutput uvValue = inputs.get("uv");

        commonShaderBuilder.addMainLine("// Sampler2D Node");
        Map<String, FieldOutput> result = new HashMap<>();
        String colorName = "color_" + nodeId;
        commonShaderBuilder.addMainLine("vec4 " + colorName + " = texture2D(" + textureValue.getSamplerRepresentation() + ", " + textureValue.getRepresentation() + ".xy + " + uvValue.getRepresentation() + " * " + textureValue.getRepresentation() + ".zw);");
        result.put("color", new DefaultFieldOutput(ShaderFieldType.Color, colorName));
        if (producedOutputs.contains("r")) {
            String name = "r_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + colorName + ".r;");
            result.put("r", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("g")) {
            String name = "g_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + colorName + ".g;");
            result.put("g", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("b")) {
            String name = "b_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + colorName + ".b;");
            result.put("b", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("a")) {
            String name = "a_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + colorName + ".a;");
            result.put("a", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        return result;
    }
}
