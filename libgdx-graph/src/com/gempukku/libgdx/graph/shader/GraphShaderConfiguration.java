package com.gempukku.libgdx.graph.shader;

import com.gempukku.libgdx.graph.shader.node.EndGraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.PropertyShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.attribute.AttributeNormalShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.attribute.AttributeUVShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.material.DiffuseTextureShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.ClampShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.DotProductShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.LerpShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.MultiplyShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.part.MergeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.part.SplitShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.texture.Sampler2DShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.value.ValueBooleanShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.value.ValueColorShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.value.ValueFloatShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.value.ValueVector2ShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.value.ValueVector3ShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.ColorShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.FloatShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.TextureShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.Vector2ShaderPropertyProducer;
import com.gempukku.libgdx.graph.shader.property.Vector3ShaderPropertyProducer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GraphShaderConfiguration {
    public static Map<String, GraphShaderNodeBuilder> graphShaderNodeBuilders = new HashMap<>();
    public static List<GraphShaderPropertyProducer> graphShaderPropertyProducers = new LinkedList<>();

    static {
        // End
        addGraphShaderNodeBuilder(new EndGraphShaderNodeBuilder());

        // Math
        addGraphShaderNodeBuilder(new MergeShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SplitShaderNodeBuilder());
        addGraphShaderNodeBuilder(new MultiplyShaderNodeBuilder());
        addGraphShaderNodeBuilder(new DotProductShaderNodeBuilder());
        addGraphShaderNodeBuilder(new LerpShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ClampShaderNodeBuilder());

        // Attributes
        addGraphShaderNodeBuilder(new AttributeNormalShaderNodeBuilder());
        addGraphShaderNodeBuilder(new AttributeUVShaderNodeBuilder());

        // Material
        addGraphShaderNodeBuilder(new DiffuseTextureShaderNodeBuilder());

        // Texture
        addGraphShaderNodeBuilder(new Sampler2DShaderNodeBuilder());

        // Values
        addGraphShaderNodeBuilder(new ValueBooleanShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ValueColorShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ValueFloatShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ValueVector2ShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ValueVector3ShaderNodeBuilder());

        // Property
        addGraphShaderNodeBuilder(new PropertyShaderNodeBuilder());

        graphShaderPropertyProducers.add(new ColorShaderPropertyProducer());
        graphShaderPropertyProducers.add(new FloatShaderPropertyProducer());
        graphShaderPropertyProducers.add(new Vector2ShaderPropertyProducer());
        graphShaderPropertyProducers.add(new Vector3ShaderPropertyProducer());
        graphShaderPropertyProducers.add(new TextureShaderPropertyProducer());
    }

    private static void addGraphShaderNodeBuilder(GraphShaderNodeBuilder builder) {
        graphShaderNodeBuilders.put(builder.getType(), builder);
    }

    private GraphShaderConfiguration() {

    }
}
