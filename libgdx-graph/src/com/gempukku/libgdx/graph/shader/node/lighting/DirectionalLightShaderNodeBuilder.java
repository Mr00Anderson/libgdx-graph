package com.gempukku.libgdx.graph.shader.node.lighting;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.lighting.DirectionalLightShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.environment.GraphShaderEnvironment;
import com.gempukku.libgdx.graph.shader.models.GraphShaderModelInstanceImpl;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DirectionalLightShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public DirectionalLightShaderNodeBuilder() {
        super(new DirectionalLightShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        final int index = ((Number) data.get("index")).intValue();

        Map<String, DefaultFieldOutput> result = new HashMap<>();
        if (producedOutputs.contains("direction")) {
            String name = "u_directionalLightDirection" + index;
            commonShaderBuilder.addUniformVariable(name, "vec3", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            if (environment != null && environment.getDirectionalLights().size > index && environment.getDirectionalLights().get(index) != null) {
                                Array<DirectionalLight> directionalLights = environment.getDirectionalLights();
                                DirectionalLight directionalLight = directionalLights.get(index);
                                shader.setUniform(location, directionalLight.direction);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f);
                            }
                        }
                    });
            result.put("direction", new DefaultFieldOutput(ShaderFieldType.Vector3, name));
        }
        if (producedOutputs.contains("color")) {
            String name = "u_directionalLightColor" + index;
            commonShaderBuilder.addUniformVariable(name, "vec4", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Camera camera, GraphShaderEnvironment environment, GraphShaderModelInstanceImpl graphShaderModelInstance, Renderable renderable) {
                            if (environment != null && environment.getDirectionalLights().size > index && environment.getDirectionalLights().get(index) != null) {
                                Array<DirectionalLight> directionalLights = environment.getDirectionalLights();
                                DirectionalLight directionalLight = directionalLights.get(index);
                                shader.setUniform(location, directionalLight.color);
                            } else {
                                shader.setUniform(location, 0f, 0f, 0f, 1f);
                            }
                        }
                    });
            result.put("color", new DefaultFieldOutput(ShaderFieldType.Color, name));
        }
        return result;
    }
}
