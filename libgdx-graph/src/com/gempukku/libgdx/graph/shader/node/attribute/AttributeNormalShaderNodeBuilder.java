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
            vertexShaderBuilder.addUniformVariable("u_normalMatrix", "mat3", false, UniformSetters.normalMatrix);
            String name = "result_" + nodeId;
            vertexShaderBuilder.addMainLine("vec3" + " " + name + " = normalize(u_normalMatrix * a_normal);");

            return Collections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        } else if (coordinates.equals("object")) {
            return Collections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, "a_normal"));
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
                vertexShaderBuilder.addUniformVariable("u_normalMatrix", "mat3", false, UniformSetters.normalMatrix);
                vertexShaderBuilder.addVaryingVariable("v_normal_world", "vec3");
                vertexShaderBuilder.addMainLine("v_normal_world = normalize(u_normalMatrix * a_normal);");

                fragmentShaderBuilder.addVaryingVariable("v_normal_world", "vec3");
            }

            return Collections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_normal_world"));
        } else if (coordinates.equals("object")) {
            if (!vertexShaderBuilder.hasVaryingVariable("v_normal_object")) {
                vertexShaderBuilder.addMainLine("// Attribute Normal Node");
                vertexShaderBuilder.addVaryingVariable("v_normal_object", "vec3");
                vertexShaderBuilder.addMainLine("v_normal_object = a_normal;");

                fragmentShaderBuilder.addVaryingVariable("v_normal_object", "vec3");
            }

            return Collections.singletonMap("normal", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_normal_object"));
        }
        throw new IllegalArgumentException();
    }
}
