package com.gempukku.libgdx.graph.ui.shader;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.math.AddShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.ClampShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.DivideShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.DotProductShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.LerpShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.MultiplyShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.PowerShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.RemapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.SubtractShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.noise.SimplexNoiseNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.noise.VoronoiDistanceNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.part.MergeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.part.SplitShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.texture.Sampler2DShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.value.ValueBooleanShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.value.ValueColorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.value.ValueFloatShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.value.ValueVector2ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.value.ValueVector3ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducerImpl;
import com.gempukku.libgdx.graph.ui.producer.value.ValueBooleanBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueColorBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.producer.value.ValueVector3BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.attribute.AttributeNormalBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.attribute.AttributePositionBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.attribute.AttributeUVBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.material.TextureAttributeBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.property.PropertyColorBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.property.PropertyFloatBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.property.PropertyTextureBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.property.PropertyVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.shader.property.PropertyVector3BoxProducer;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class UIShaderConfiguration implements UIGraphConfiguration<ShaderFieldType> {
    public static Set<GraphBoxProducer<ShaderFieldType>> graphBoxProducers = new LinkedHashSet<>();
    public static Map<String, PropertyBoxProducer<ShaderFieldType>> propertyProducers = new LinkedHashMap<>();

    static {
        graphBoxProducers.add(new EndShaderBoxProducer());
        graphBoxProducers.add(new PropertyShaderGraphBoxProducer());

        graphBoxProducers.add(new AttributePositionBoxProducer());
        graphBoxProducers.add(new AttributeNormalBoxProducer());
        graphBoxProducers.add(new AttributeUVBoxProducer());

        graphBoxProducers.add(new TextureAttributeBoxProducer("AmbientTexture", "Ambient texture"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("BumpTexture", "Bump texture"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("DiffuseTexture", "Diffuse texture"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("EmissiveTexture", "Emissive texture"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("NormalTexture", "Normal texture"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("ReflectionTexture", "Reflection texture"));
        graphBoxProducers.add(new TextureAttributeBoxProducer("SpecularTexture", "Specular texture"));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new Sampler2DShaderNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new AddShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new SubtractShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new MultiplyShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new DivideShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new PowerShaderNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new SplitShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new MergeShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new DotProductShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new LerpShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new RemapShaderNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ClampShaderNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new SimplexNoiseNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new VoronoiDistanceNodeConfiguration()));

        graphBoxProducers.add(new ValueColorBoxProducer<ShaderFieldType>(new ValueColorShaderNodeConfiguration()));
        graphBoxProducers.add(new ValueFloatBoxProducer<ShaderFieldType>(new ValueFloatShaderNodeConfiguration()));
        graphBoxProducers.add(new ValueVector2BoxProducer<ShaderFieldType>(new ValueVector2ShaderNodeConfiguration()));
        graphBoxProducers.add(new ValueVector3BoxProducer<ShaderFieldType>(new ValueVector3ShaderNodeConfiguration()));
        graphBoxProducers.add(new ValueBooleanBoxProducer<ShaderFieldType>(new ValueBooleanShaderNodeConfiguration()));

        propertyProducers.put("Float", new PropertyFloatBoxProducer());
        propertyProducers.put("Vector2", new PropertyVector2BoxProducer());
        propertyProducers.put("Vector3", new PropertyVector3BoxProducer());
        propertyProducers.put("Color", new PropertyColorBoxProducer());
        propertyProducers.put("Texture", new PropertyTextureBoxProducer());
    }

    @Override
    public Set<GraphBoxProducer<ShaderFieldType>> getGraphBoxProducers() {
        return graphBoxProducers;
    }

    @Override
    public Map<String, PropertyBoxProducer<ShaderFieldType>> getPropertyBoxProducers() {
        return propertyProducers;
    }
}
