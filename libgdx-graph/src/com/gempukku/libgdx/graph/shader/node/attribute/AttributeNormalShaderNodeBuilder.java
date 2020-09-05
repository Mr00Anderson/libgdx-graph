package com.gempukku.libgdx.graph.shader.node.attribute;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.attribute.AttributeNormalShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class AttributeNormalShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public AttributeNormalShaderNodeBuilder() {
        super(new AttributeNormalShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(ShaderProgram.NORMAL_ATTRIBUTE, "vec3");

        String coordinates = (String) data.get("coordinates");
        if (coordinates.equals("world")) {
            vertexShaderBuilder.addMainLine("// Attribute Normal Node");
            vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, UniformSetters.worldTrans);
            String name = "result_" + nodeId;
            vertexShaderBuilder.addMainLine("vec3" + " " + name + " = normalize((u_worldTrans * skinning * vec4(a_normal, 0.0)).xyz);");

            return Collections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        } else if (coordinates.equals("object")) {
            String name = "result_" + nodeId;
            vertexShaderBuilder.addMainLine("vec3" + " " + name + " = normalize((skinning * vec4(a_normal, 0.0)).xyz);");
            return Collections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Map<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JSONObject
            data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder
                                                                        vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext
                                                                        graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(ShaderProgram.NORMAL_ATTRIBUTE, "vec3");

        String coordinates = (String) data.get("coordinates");
        if (coordinates.equals("world")) {
            if (!vertexShaderBuilder.hasVaryingVariable("v_normal_world")) {
                vertexShaderBuilder.addMainLine("// Attribute Normal Node");
                vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, UniformSetters.worldTrans);
                vertexShaderBuilder.addVaryingVariable("v_normal_world", "vec3");
                vertexShaderBuilder.addMainLine("v_normal_world = normalize((u_worldTrans * skinning * vec4(a_normal, 0.0)).xyz);");

                fragmentShaderBuilder.addVaryingVariable("v_normal_world", "vec3");
            }

            return Collections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_normal_world"));
        } else if (coordinates.equals("object")) {
            if (!vertexShaderBuilder.hasVaryingVariable("v_normal_object")) {
                vertexShaderBuilder.addMainLine("// Attribute Normal Node");
                vertexShaderBuilder.addVaryingVariable("v_normal_object", "vec3");
                vertexShaderBuilder.addMainLine("v_normal_object = normalize((skinning * vec4(a_normal, 0.0)).xyz);");

                fragmentShaderBuilder.addVaryingVariable("v_normal_object", "vec3");
            }

            return Collections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_normal_object"));
        }
        throw new IllegalArgumentException();
    }
}
