package com.gempukku.libgdx.graph.shader.node.lighting;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.lighting.SpotLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstanceImpl;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SpotLightShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SpotLightShaderNodeBuilder() {
        super(new SpotLightShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final int index = ((Number) data.get("index")).intValue();

        Map<String, DefaultFieldOutput> result = new HashMap<>();
        if (producedOutputs.contains("position")) {
            String name = "u_spotLightPosition" + index;
            commonShaderBuilder.addUniformVariable(name, "vec3", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<SpotLight> spotLights = environment.getSpotLights();
                                SpotLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.position);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                            }
                        }
                    });
            result.put("position", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("direction")) {
            String name = "u_spotLightDirection" + index;
            commonShaderBuilder.addUniformVariable(name, "vec3", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<SpotLight> spotLights = environment.getSpotLights();
                                SpotLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.direction);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                            }
                        }
                    });
            result.put("direction", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("color")) {
            String name = "u_spotLightColor" + index;
            commonShaderBuilder.addUniformVariable(name, "vec4", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<SpotLight> spotLights = environment.getSpotLights();
                                SpotLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.color);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f, 1f);
                            }
                        }
                    });
            result.put("color", new DefaultFieldOutput(ShaderFieldType.Color, name));
        }
        if (producedOutputs.contains("intensity")) {
            String name = "u_spotLightIntensity" + index;
            commonShaderBuilder.addUniformVariable(name, "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<SpotLight> spotLights = environment.getSpotLights();
                                SpotLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.intensity);
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    });
            result.put("intensity", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("cutOffAngle")) {
            String name = "u_spotLightCutOffAngle" + index;
            commonShaderBuilder.addUniformVariable(name, "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<SpotLight> spotLights = environment.getSpotLights();
                                SpotLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.cutoffAngle);
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    });
            result.put("cutOffAngle", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("exponent")) {
            String name = "u_spotLightExponent" + index;
            commonShaderBuilder.addUniformVariable(name, "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            if (environment != null && environment.getSpotLights().size > index && environment.getSpotLights().get(index) != null) {
                                Array<SpotLight> spotLights = environment.getSpotLights();
                                SpotLight spotLight = spotLights.get(index);
                                shader.setUniform(location, spotLight.exponent);
                            } else {
                                shader.setUniform(location, 0f);
                            }
                        }
                    });
            result.put("exponent", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        return result;
    }
}
