package com.gempukku.libgdx.graph.shader.node.attribute;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.attribute.AttributePositionShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class AttributePositionShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public AttributePositionShaderNodeBuilder() {
        super(new AttributePositionShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(ShaderProgram.POSITION_ATTRIBUTE, "vec3");

        String coordinates = (String) data.get("coordinates");
        if (coordinates.equals("world")) {
            vertexShaderBuilder.addMainLine("// Attribute Position Node");
            vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, UniformSetters.worldTrans);
            String name = "result_" + nodeId;
            vertexShaderBuilder.addMainLine("vec3" + " " + name + " = (u_worldTrans * vec4(a_position, 1.0)).xyz;");

            return Collections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        } else if (coordinates.equals("object")) {
            return Collections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, "a_position"));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Map<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        vertexShaderBuilder.addAttributeVariable(ShaderProgram.POSITION_ATTRIBUTE, "vec3");

        String coordinates = (String) data.get("coordinates");
        if (coordinates.equals("world")) {
            if (!vertexShaderBuilder.hasVaryingVariable("v_position_world")) {
                vertexShaderBuilder.addMainLine("// Attribute Position Node");
                vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, UniformSetters.worldTrans);
                vertexShaderBuilder.addMainLine("v_position_world = (u_worldTrans * vec4(a_position, 1.0)).xyz;");

                fragmentShaderBuilder.addVaryingVariable("v_position_world", "vec3");
            }

            return Collections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_position_world"));
        } else if (coordinates.equals("object")) {
            if (!vertexShaderBuilder.hasVaryingVariable("v_position_object")) {
                vertexShaderBuilder.addMainLine("// Attribute Position Node");
                vertexShaderBuilder.addVaryingVariable("v_position_object", "vec3");
                vertexShaderBuilder.addMainLine("v_position_object = a_position;");

                fragmentShaderBuilder.addVaryingVariable("v_position_object", "vec3");
            }

            return Collections.singletonMap("position", new DefaultFieldOutput(ShaderFieldType.Vector3, "v_position_object"));
        }
        throw new IllegalArgumentException();
    }
}
