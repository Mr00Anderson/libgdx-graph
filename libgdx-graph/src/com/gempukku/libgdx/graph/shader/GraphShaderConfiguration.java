package com.gempukku.libgdx.graph.shader;

import com.gempukku.libgdx.graph.shader.node.EndGraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.PropertyShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.attribute.AttributeNormalShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.attribute.AttributePositionShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.attribute.AttributeUVShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.material.TextureAttributeShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.AddShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.ClampShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.DivideShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.DotProductShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.LerpShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.MultiplyShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.PowerShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.RemapShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.math.SubtractShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.noise.SimplexNoiseShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.noise.VoronoiDistanceShaderNodeBuilder;
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
        addGraphShaderNodeBuilder(new AddShaderNodeBuilder());
        addGraphShaderNodeBuilder(new SubtractShaderNodeBuilder());
        addGraphShaderNodeBuilder(new MultiplyShaderNodeBuilder());
        addGraphShaderNodeBuilder(new DivideShaderNodeBuilder());
        addGraphShaderNodeBuilder(new DotProductShaderNodeBuilder());
        addGraphShaderNodeBuilder(new LerpShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ClampShaderNodeBuilder());
        addGraphShaderNodeBuilder(new PowerShaderNodeBuilder());
        addGraphShaderNodeBuilder(new RemapShaderNodeBuilder());

        // Attributes
        addGraphShaderNodeBuilder(new AttributePositionShaderNodeBuilder());
        addGraphShaderNodeBuilder(new AttributeNormalShaderNodeBuilder());
        addGraphShaderNodeBuilder(new AttributeUVShaderNodeBuilder());

        // Material
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("AmbientTexture", "Ambient texture", "ambient"));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("BumpTexture", "Bump texture", "bump"));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("DiffuseTexture", "Diffuse texture", "diffuse"));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("EmissiveTexture", "Emissive texture", "emissive"));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("NormalTexture", "Normal texture", "normal"));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("ReflectionTexture", "Reflection texture", "reflection"));
        addGraphShaderNodeBuilder(new TextureAttributeShaderNodeBuilder("SpecularTexture", "Specular texture", "specular"));

        // Texture
        addGraphShaderNodeBuilder(new Sampler2DShaderNodeBuilder());

        // Noise
        addGraphShaderNodeBuilder(new SimplexNoiseShaderNodeBuilder());
        addGraphShaderNodeBuilder(new VoronoiDistanceShaderNodeBuilder());

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
