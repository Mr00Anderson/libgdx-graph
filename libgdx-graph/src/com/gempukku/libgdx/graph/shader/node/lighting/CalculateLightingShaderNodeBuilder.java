package com.gempukku.libgdx.graph.shader.node.lighting;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.environment.SpotLight;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderConfig;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.lighting.CalculateLightingShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstanceImpl;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CalculateLightingShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public CalculateLightingShaderNodeBuilder() {
        super(new CalculateLightingShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildVertexNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        throw new UnsupportedOperationException("At the moment light calculation is not available in vertex shader");
    }

    @Override
    public Map<String, ? extends FieldOutput> buildFragmentNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, final GraphShaderContext graphShaderContext, GraphShader graphShader) {
        fragmentShaderBuilder.addStructure("Lighting",
                "  vec3 diffuse;\n" +
                        "  vec3 specular;\n");

        fragmentShaderBuilder.addUniformVariable("u_ambientLight", "vec3", true, UniformSetters.ambientLight);

        final int numDirectionalLights = GraphShaderConfig.getMaxNumberOfDirectionalLights();
        passDirectionalLights(fragmentShaderBuilder, numDirectionalLights);
        final int numPointLights = GraphShaderConfig.getMaxNumberOfPointLights();
        passPointLights(fragmentShaderBuilder, numPointLights);
        final int numSpotLights = GraphShaderConfig.getMaxNumberOfSpotlights();
        passSpotLights(fragmentShaderBuilder, numSpotLights);

        FieldOutput positionValue = inputs.get("position");
        FieldOutput normalValue = inputs.get("normal");
        FieldOutput albedoValue = inputs.get("albedo");
        FieldOutput emissionValue = inputs.get("emission");
        FieldOutput specularValue = inputs.get("specular");
        FieldOutput shininessValue = inputs.get("shininess");

        String position = positionValue.getRepresentation();
        String normal = normalValue.getRepresentation();
        String albedo = albedoValue != null ? albedoValue.getRepresentation() : "vec3(0.0)";
        String emission = emissionValue != null ? emissionValue.getRepresentation() : "vec3(0.0)";
        String specular = specularValue != null ? specularValue.getRepresentation() : "vec3(1.0)";
        String shininess = shininessValue != null ? shininessValue.getRepresentation() : "32.0";

        fragmentShaderBuilder.addMainLine("// Calculate Lighting node");
        String lightingVariable = "lighting_" + nodeId;
        fragmentShaderBuilder.addMainLine("Lighting " + lightingVariable + " = Lighting(vec3(0.0), vec3(0.0));");
        if (numDirectionalLights > 0)
            fragmentShaderBuilder.addMainLine(lightingVariable + " = getDirectionalLightContribution(" + position + ", " + normal + ", " + shininess + ", " + lightingVariable + ");");
        if (numPointLights > 0)
            fragmentShaderBuilder.addMainLine(lightingVariable + " = getPointLightContribution(" + position + ", " + normal + ", " + shininess + ", " + lightingVariable + ");");
        if (numSpotLights > 0)
            fragmentShaderBuilder.addMainLine(lightingVariable + " = getSpotLightContribution(" + position + ", " + normal + ", " + shininess + ", " + lightingVariable + ");");

        ShaderFieldType resultType = ShaderFieldType.Vector3;
        Map<String, DefaultFieldOutput> result = new HashMap<>();
        if (producedOutputs.contains("output")) {
            String name = "color_" + nodeId;
            fragmentShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = " + emission + ".rgb + u_ambientLight * " + albedo + ".rgb;");
            fragmentShaderBuilder.addMainLine(name + " += " + lightingVariable + ".diffuse * " + albedo + ".rgb + " + lightingVariable + ".specular * " + specular + ".rgb;");
            result.put("output", new DefaultFieldOutput(resultType, name));
        } else if (producedOutputs.contains("diffuse")) {
            result.put("diffuse", new DefaultFieldOutput(resultType, lightingVariable + ".diffuse"));
        } else if (producedOutputs.contains("specular")) {
            result.put("specular", new DefaultFieldOutput(resultType, lightingVariable + ".specular"));
        }

        return result;
    }

    private void passSpotLights(FragmentShaderBuilder fragmentShaderBuilder, final int numSpotLights) {
        if (numSpotLights > 0) {
            fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec4", true, UniformSetters.cameraPosition);
            fragmentShaderBuilder.addUniformVariable("u_spotLightCount", "int", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            shader.setUniform(location, environment.getSpotLights().size);
                        }
                    });
            fragmentShaderBuilder.addStructure("SpotLight",
                    "  vec3 color;\n" +
                            "  vec3 position;\n" +
                            "  vec3 direction;\n" +
                            "  float cutoffAngle;\n" +
                            "  float exponent;\n");
            fragmentShaderBuilder.addStructArrayUniformVariable("u_spotLights", new String[]{"color", "position", "direction", "cutoffAngle", "exponent"}, numSpotLights, "SpotLight", true,
                    new UniformRegistry.StructArrayUniformSetter() {
                        @Override
                        public void set(BasicShader shader, int startingLocation, int[] fieldOffsets, int structSize, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            Array<SpotLight> spots = null;
                            if (environment != null) {
                                spots = environment.getSpotLights();
                            }

                            for (int i = 0; i < numSpotLights; i++) {
                                int location = startingLocation + i * structSize;
                                if (spots != null && i < spots.size) {
                                    SpotLight spotLight = spots.get(i);

                                    shader.setUniform(location, spotLight.color.r * spotLight.intensity,
                                            spotLight.color.g * spotLight.intensity, spotLight.color.b * spotLight.intensity);
                                    shader.setUniform(location + fieldOffsets[1], spotLight.position.x, spotLight.position.y,
                                            spotLight.position.z);
                                    shader.setUniform(location + fieldOffsets[2], spotLight.direction.x, spotLight.direction.y,
                                            spotLight.direction.z);
                                    shader.setUniform(location + fieldOffsets[3], spotLight.cutoffAngle);
                                    shader.setUniform(location + fieldOffsets[4], spotLight.exponent);
                                } else {
                                    shader.setUniform(location, 0f, 0f, 0f);
                                    shader.setUniform(location + fieldOffsets[1], 0f, 0f, 0f);
                                    shader.setUniform(location + fieldOffsets[2], 0f, 0f, 0f);
                                    shader.setUniform(location + fieldOffsets[3], 0f);
                                    shader.setUniform(location + fieldOffsets[4], 0f);
                                }
                            }
                        }
                    });
            if (!fragmentShaderBuilder.containsFunction("getSpotLightContribution")) {
                fragmentShaderBuilder.addFunction("getSpotLightContribution",
                        GLSLFragmentReader.getFragment("spotLightContribution",
                                Collections.singletonMap("NUM_SPOT_LIGHTS", String.valueOf(numSpotLights))));
            }
        } else {
            if (!fragmentShaderBuilder.containsFunction("getSpotLightContribution")) {
                fragmentShaderBuilder.addFunction("getSpotLightContribution",
                        "Lighting getSpotLightContribution(vec4 pos, vec3 normal, float shininess, Lighting lighting) {\n" +
                                "  return lighting;\n" +
                                "}\n");
            }
        }
    }

    private void passPointLights(FragmentShaderBuilder fragmentShaderBuilder, final int numPointLights) {
        if (numPointLights > 0) {
            fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec4", true, UniformSetters.cameraPosition);
            fragmentShaderBuilder.addUniformVariable("u_pointLightCount", "int", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            shader.setUniform(location, environment.getPointLights().size);
                        }
                    });
            fragmentShaderBuilder.addStructure("PointLight",
                    "  vec3 color;\n" +
                            "  vec3 position;\n");
            fragmentShaderBuilder.addStructArrayUniformVariable("u_pointLights", new String[]{"color", "position"}, numPointLights, "PointLight", true,
                    new UniformRegistry.StructArrayUniformSetter() {
                        @Override
                        public void set(BasicShader shader, int startingLocation, int[] fieldOffsets, int structSize, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            Array<PointLight> points = null;
                            if (environment != null) {
                                points = environment.getPointLights();
                            }

                            for (int i = 0; i < numPointLights; i++) {
                                int location = startingLocation + i * structSize;
                                if (points != null && i < points.size) {
                                    PointLight pointLight = points.get(i);

                                    shader.setUniform(location, pointLight.color.r * pointLight.intensity,
                                            pointLight.color.g * pointLight.intensity, pointLight.color.b * pointLight.intensity);
                                    shader.setUniform(location + fieldOffsets[1], pointLight.position.x, pointLight.position.y,
                                            pointLight.position.z);
                                } else {
                                    shader.setUniform(location, 0f, 0f, 0f);
                                    shader.setUniform(location + fieldOffsets[1], 0f, 0f, 0f);
                                }
                            }
                        }
                    });
            if (!fragmentShaderBuilder.containsFunction("getPointLightContribution")) {
                fragmentShaderBuilder.addFunction("getPointLightContribution",
                        GLSLFragmentReader.getFragment("pointLightContribution",
                                Collections.singletonMap("NUM_POINT_LIGHTS", String.valueOf(numPointLights))));
            }
        } else {
            if (!fragmentShaderBuilder.containsFunction("getPointLightContribution")) {
                fragmentShaderBuilder.addFunction("getPointLightContribution",
                        "Lighting getPointLightContribution(vec4 pos, vec3 normal, float shininess,  lighting) {\n" +
                                "  return lighting;\n" +
                                "}\n");
            }
        }
    }

    private void passDirectionalLights(FragmentShaderBuilder fragmentShaderBuilder, final int numDirectionalLights) {
        if (numDirectionalLights > 0) {
            fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec4", true, UniformSetters.cameraPosition);
            fragmentShaderBuilder.addUniformVariable("u_directionalLightCount", "int", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            shader.setUniform(location, environment.getDirectionalLights().size);
                        }
                    });
            fragmentShaderBuilder.addStructure("DirectionalLight",
                    "  vec3 color;\n" +
                            "  vec3 direction;\n");
            fragmentShaderBuilder.addStructArrayUniformVariable("u_dirLights", new String[]{"color", "direction"}, numDirectionalLights, "DirectionalLight", true,
                    new UniformRegistry.StructArrayUniformSetter() {
                        @Override
                        public void set(BasicShader shader, int startingLocation, int[] fieldOffsets, int structSize, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            Array<DirectionalLight> dirs = null;
                            if (environment != null) {
                                dirs = environment.getDirectionalLights();
                            }

                            for (int i = 0; i < numDirectionalLights; i++) {
                                int location = startingLocation + i * structSize;
                                if (dirs != null && i < dirs.size) {
                                    DirectionalLight directionalLight = dirs.get(i);

                                    shader.setUniform(location, directionalLight.color.r, directionalLight.color.g,
                                            directionalLight.color.b);
                                    shader.setUniform(location + fieldOffsets[1], directionalLight.direction.x,
                                            directionalLight.direction.y, directionalLight.direction.z);
                                } else {
                                    shader.setUniform(location, 0f, 0f, 0f);
                                    shader.setUniform(location + fieldOffsets[1], 0f, 0f, 0f);
                                }
                            }
                        }
                    });
            if (!fragmentShaderBuilder.containsFunction("getDirectionalLightContribution")) {
                fragmentShaderBuilder.addFunction("getDirectionalLightContribution",
                        GLSLFragmentReader.getFragment("directionalLightContribution",
                                Collections.singletonMap("NUM_DIRECTIONAL_LIGHTS", String.valueOf(numDirectionalLights))));
            }
        } else {
            if (!fragmentShaderBuilder.containsFunction("getDirectionalLightContribution")) {
                fragmentShaderBuilder.addFunction("getDirectionalLightContribution",
                        "Lighting getDirectionalLightContribution(vec4 pos, vec3 normal, float shininess, Lighting lighting) {\n" +
                                "  return lighting;\n" +
                                "}\n");
            }
        }
    }
}
