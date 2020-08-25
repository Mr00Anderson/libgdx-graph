package com.gempukku.libgdx.graph.shader.node.material;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.gempukku.libgdx.WhitePixel;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.UniformRegistry;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.material.TextureAttributeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TextureAttributeShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    private String symbol;

    public TextureAttributeShaderNodeBuilder(String type, String name, String symbol) {
        super(new TextureAttributeShaderNodeConfiguration(type, name));
        this.symbol = symbol;
    }

    @Override
    public Map<String, ? extends FieldOutput> buildNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                        VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext) {
        String textureName = "u_" + symbol + "Texture";
        String transformName = "u_" + symbol + "UVTransform";
        if (designTime) {
            Texture texture = null;
            if (data != null) {
                String previewPath = (String) data.get("previewPath");
                if (previewPath != null) {
                    try {
                        texture = new Texture(Gdx.files.absolute(previewPath));
                        graphShaderContext.addManagedResource(texture);
                    } catch (Exception exp) {
                        // Ignore
                    }
                }
            }
            if (texture == null)
                texture = WhitePixel.sharedInstance.texture;


            final Texture finalTexture = texture;
            fragmentShaderBuilder.addUniformVariable(textureName, "sampler2D", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Renderable renderable, Attributes combinedAttributes) {
                            shader.setUniform(location, finalTexture);
                        }
                    });
            fragmentShaderBuilder.addUniformVariable(transformName, "vec4", false,
                    new UniformRegistry.UniformSetter() {
                        @Override
                        public void set(BasicShader shader, int location, Renderable renderable, Attributes combinedAttributes) {
                            shader.setUniform(location, 0f, 0f, 1f, 1f);
                        }
                    });
        } else {
            fragmentShaderBuilder.addUniformVariable(textureName, "sampler2D", false,
                    UniformSetters.diffuseTexture);
            fragmentShaderBuilder.addUniformVariable(transformName, "vec4", false,
                    UniformSetters.diffuseUVTransform);
        }

        return Collections.singletonMap("texture", new DefaultFieldOutput(ShaderFieldType.TextureRegion, transformName, textureName));
    }
}
