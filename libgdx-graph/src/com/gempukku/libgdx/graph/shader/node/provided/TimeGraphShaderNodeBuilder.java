package com.gempukku.libgdx.graph.shader.node.provided;

import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.MathUtils;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.provided.TimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TimeGraphShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public TimeGraphShaderNodeBuilder() {
        super(new TimeShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                                 CommonShaderBuilder commonShaderBuilder, final GraphShaderContext graphShaderContext, GraphShader graphShader) {
        Map<String, FieldOutput> result = new HashMap<>();
        if (producedOutputs.contains("time")) {
            commonShaderBuilder.addUniformVariable("u_time", "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Renderable renderable, Attributes combinedAttributes) {
                            shader.setUniform(location, graphShaderContext.getTimeProvider().getTime());
                        }
                    });
            result.put("time", new DefaultFieldOutput(ShaderFieldType.Float, "u_time"));
        }
        if (producedOutputs.contains("sinTime")) {
            commonShaderBuilder.addUniformVariable("u_sinTime", "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Renderable renderable, Attributes combinedAttributes) {
                            shader.setUniform(location, MathUtils.sin(graphShaderContext.getTimeProvider().getTime()));
                        }
                    });
            result.put("sinTime", new DefaultFieldOutput(ShaderFieldType.Float, "u_sinTime"));
        }
        if (producedOutputs.contains("cosTime")) {
            commonShaderBuilder.addUniformVariable("u_cosTime", "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Renderable renderable, Attributes combinedAttributes) {
                            shader.setUniform(location, MathUtils.cos(graphShaderContext.getTimeProvider().getTime()));
                        }
                    });
            result.put("cosTime", new DefaultFieldOutput(ShaderFieldType.Float, "u_cosTime"));
        }
        if (producedOutputs.contains("deltaTime")) {
            commonShaderBuilder.addUniformVariable("u_deltaTime", "float", true,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Renderable renderable, Attributes combinedAttributes) {
                            shader.setUniform(location, graphShaderContext.getTimeProvider().getDelta());
                        }
                    });
            result.put("deltaTime", new DefaultFieldOutput(ShaderFieldType.Float, "u_deltaTime"));
        }
        return result;
    }
}
