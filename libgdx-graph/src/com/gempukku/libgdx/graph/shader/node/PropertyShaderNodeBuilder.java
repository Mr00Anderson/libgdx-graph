package com.gempukku.libgdx.graph.shader.node;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.graph.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstanceImpl;
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

        return new PropertyNodeConfiguration<ShaderFieldType>(name, propertyType);
    }

    @Override
    public Map<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        return buildCommonNode(designTime, nodeId, data, inputs, producedOutputs, vertexShaderBuilder, graphShaderContext, graphShader);
    }

    @Override
    public Map<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                                VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        return buildCommonNode(designTime, nodeId, data, inputs, producedOutputs, fragmentShaderBuilder, graphShaderContext, graphShader);
    }

    private Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                               CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final String name = (String) data.get("name");
        final ShaderFieldType propertyType = ShaderFieldType.valueOf((String) data.get("type"));

        switch (propertyType) {
            case Color:
                return buildColorPropertyNode(nodeId, name, graphShaderContext, commonShaderBuilder);
            case Float:
                return buildFloatPropertyNode(nodeId, name, graphShaderContext, commonShaderBuilder);
            case Vector2:
                return buildVector2PropertyNode(nodeId, name, graphShaderContext, commonShaderBuilder);
            case Vector3:
                return buildVector3PropertyNode(nodeId, name, graphShaderContext, commonShaderBuilder);
            case TextureRegion:
                return buildTexturePropertyNode(nodeId, name, graphShaderContext, commonShaderBuilder);
        }

        return null;
    }


    private Map<String, DefaultFieldOutput> buildColorPropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                   CommonShaderBuilder commonShaderBuilder) {
        String variableName = "u_property_" + nodeId;
        commonShaderBuilder.addUniformVariable(variableName, "vec4", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                        Object value = graphShaderModelInstance.getProperty(name);
                        if (!(value instanceof Color))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, (Color) value);
                    }
                }, "Property - " + name);

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Color, variableName));
    }

    private Map<String, DefaultFieldOutput> buildFloatPropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                   CommonShaderBuilder commonShaderBuilder) {
        String variableName = "u_property_" + nodeId;
        commonShaderBuilder.addUniformVariable(variableName, "float", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                        Object value = graphShaderModelInstance.getProperty(name);
                        if (!(value instanceof Number))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, ((Number) value).floatValue());
                    }
                }, "Property - " + name);

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Float, variableName));
    }

    private Map<String, DefaultFieldOutput> buildVector2PropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                     CommonShaderBuilder commonShaderBuilder) {
        String variableName = "u_property_" + nodeId;
        commonShaderBuilder.addUniformVariable(variableName, "vec2", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                        Object value = graphShaderModelInstance.getProperty(name);
                        if (!(value instanceof Vector2))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, (Vector2) value);
                    }
                }, "Property - " + name);

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector2, variableName));
    }

    private Map<String, DefaultFieldOutput> buildVector3PropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                     CommonShaderBuilder commonShaderBuilder) {
        String variableName = "u_property_" + nodeId;
        commonShaderBuilder.addUniformVariable(variableName, "vec3", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                        Object value = graphShaderModelInstance.getProperty(name);
                        if (!(value instanceof Vector3))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, (Vector3) value);
                    }
                }, "Property - " + name);

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.Vector3, variableName));
    }

    private Map<String, DefaultFieldOutput> buildTexturePropertyNode(String nodeId, final String name, final GraphShaderContext graphShaderContext,
                                                                     CommonShaderBuilder commonShaderBuilder) {
        String textureVariableName = "u_property_" + nodeId;
        String uvTransformVariableName = "u_uvTransform_" + nodeId;
        commonShaderBuilder.addUniformVariable(textureVariableName, "sampler2D", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                        Object value = graphShaderModelInstance.getProperty(name);
                        if (!(value instanceof TextureRegion))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        shader.setUniform(location, ((TextureRegion) value).getTexture());
                    }
                }, "Texture property - " + name);
        commonShaderBuilder.addUniformVariable(uvTransformVariableName, "vec4", false,
                new UniformRegistry.UniformSetter() {
                    @Override
                    public void set(BasicShader shader, int location, Camera camera, Environment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                        Object value = graphShaderModelInstance.getProperty(name);
                        if (!(value instanceof TextureRegion))
                            value = graphShaderContext.getPropertySource(name).getDefaultValue();
                        TextureRegion region = (TextureRegion) value;
                        shader.setUniform(location,
                                region.getU(), region.getV(),
                                region.getU2() - region.getU(),
                                region.getV2() - region.getV());
                    }
                }, "Texture UV property - " + name);

        return Collections.singletonMap("value", new DefaultFieldOutput(ShaderFieldType.TextureRegion, uvTransformVariableName, textureVariableName));
    }
}
