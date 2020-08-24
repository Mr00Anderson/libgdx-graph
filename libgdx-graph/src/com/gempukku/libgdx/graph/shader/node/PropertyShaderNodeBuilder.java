package com.gempukku.libgdx.graph.shader.node;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.NodeConfiguration;
import com.gempukku.libgdx.graph.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShaderAttribute;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class PropertyShaderNodeBuilder implements GraphShaderNodeBuilder {
    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public NodeConfiguration<ShaderFieldType> getConfiguration(JSONObject data) {
        final String name = (String) data.get("name");
        final ShaderFieldType propertyType = ShaderFieldType.valueOf((String) data.get("type"));

        return new PropertyNodeConfiguration<ShaderFieldType>(propertyType.name(), name, propertyType);
    }

    @Override
    public Map<String, ? extends FieldOutput> buildNode(String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                        VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext) {
        final String name = (String) data.get("name");
        final ShaderFieldType propertyType = ShaderFieldType.valueOf((String) data.get("type"));

        switch (propertyType) {
            case Color:
                return buildColorPropertyNode(nodeId, name, graphShaderContext, vertexShaderBuilder, fragmentShaderBuilder);
            case Float:
                return buildFloatPropertyNode(nodeId, name, graphShaderContext, vertexShaderBuilder, fragmentShaderBuilder);
            case Vector2:
                return buildVector2PropertyNode(nodeId, name, graphShaderContext, vertexShaderBuilder, fragmentShaderBuilder);
            case Vector3:
                return buildVector3PropertyNode(nodeId, name, graphShaderContext, vertexShaderBuilder, fragmentShaderBuilder);
        }

        return null;
    }

    private Map<String, DefaultFieldOutput> buildColorPropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                   VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        String variableName = "u_value_" + nodeId;
        fragmentShaderBuilder.addUniformVariable(variableName, "vec4", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, Renderable renderable, Attributes combinedAttributes) {
                        GraphShaderAttribute graphShaderAttribute = renderable.material.get(GraphShaderAttribute.class, GraphShaderAttribute.GraphShader);
                        Object value = graphShaderAttribute.getVariable(name);
                        if (!(value instanceof Color))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, (Color) value);
                    }
                });

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Color, variableName));
    }

    private Map<String, DefaultFieldOutput> buildFloatPropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                   VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        String variableName = "u_value_" + nodeId;
        fragmentShaderBuilder.addUniformVariable(variableName, "float", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, Renderable renderable, Attributes combinedAttributes) {
                        GraphShaderAttribute graphShaderAttribute = renderable.material.get(GraphShaderAttribute.class, GraphShaderAttribute.GraphShader);
                        Object value = graphShaderAttribute.getVariable(name);
                        if (!(value instanceof Number))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, ((Number) value).floatValue());
                    }
                });

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Float, variableName));
    }

    private Map<String, DefaultFieldOutput> buildVector2PropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                     VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        String variableName = "u_value_" + nodeId;
        fragmentShaderBuilder.addUniformVariable(variableName, "vec2", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, Renderable renderable, Attributes combinedAttributes) {
                        GraphShaderAttribute graphShaderAttribute = renderable.material.get(GraphShaderAttribute.class, GraphShaderAttribute.GraphShader);
                        Object value = graphShaderAttribute.getVariable(name);
                        if (!(value instanceof Vector2))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, (Vector2) value);
                    }
                });

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector2, variableName));
    }

    private Map<String, DefaultFieldOutput> buildVector3PropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                     VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        String variableName = "u_value_" + nodeId;
        fragmentShaderBuilder.addUniformVariable(variableName, "vec3", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, Renderable renderable, Attributes combinedAttributes) {
                        GraphShaderAttribute graphShaderAttribute = renderable.material.get(GraphShaderAttribute.class, GraphShaderAttribute.GraphShader);
                        Object value = graphShaderAttribute.getVariable(name);
                        if (!(value instanceof Vector3))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, (Vector3) value);
                    }
                });

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector3, variableName));
    }
}
