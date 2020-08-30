package com.gempukku.libgdx.graph.shader.node.attribute;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.attribute.AttributeUVShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AttributeUVShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    private static List<String> channels = Arrays.asList("UV0", "UV1", "UV2", "UV3");

    public AttributeUVShaderNodeBuilder() {
        super(new AttributeUVShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String channel = (String) data.get("channel");
        int unit = channels.indexOf(channel);

        String attributeName = ShaderProgram.TEXCOORD_ATTRIBUTE + unit;
        vertexShaderBuilder.addAttributeVariable(attributeName, "vec2");

        return Collections.singletonMap("uv", new DefaultFieldOutput(ShaderFieldType.Vector2, attributeName));
    }

    @Override
    public Map<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        String channel = (String) data.get("channel");
        int unit = channels.indexOf(channel);

        String attributeName = ShaderProgram.TEXCOORD_ATTRIBUTE + unit;
        vertexShaderBuilder.addAttributeVariable(attributeName, "vec2");

        String name = "v_uv_" + unit;
        if (!vertexShaderBuilder.hasVaryingVariable(name)) {
            vertexShaderBuilder.addMainLine("// Attribute UV Node");
            vertexShaderBuilder.addVaryingVariable(name, "vec2");
            vertexShaderBuilder.addMainLine(name + " = " + attributeName + ";");

            fragmentShaderBuilder.addVaryingVariable(name, "vec2");
        }

        return Collections.singletonMap("uv", new DefaultFieldOutput(ShaderFieldType.Vector2, name));
    }
}
