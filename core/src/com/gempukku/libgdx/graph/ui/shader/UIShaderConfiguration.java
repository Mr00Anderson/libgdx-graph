package com.gempukku.libgdx.graph.ui.shader;

import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.color.IntensityShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.arithmetic.AddShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.arithmetic.DivideShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.arithmetic.MultiplyShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.arithmetic.SubtractShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.common.AbsShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.common.CeilingShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.common.ClampShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.common.FloorShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.common.FractionalPartShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.common.LerpShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.common.MaximumShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.common.MinimumShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.common.ModuloShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.common.SignShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.common.SmoothstepShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.common.StepShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.exponential.ExponentialBase2ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.exponential.ExponentialShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.exponential.InverseSquareRootShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.exponential.LogarithmBase2ShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.exponential.NaturalLogarithmShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.exponential.PowerShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.exponential.SquareRootShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.geometric.CrossProductShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.geometric.DistanceShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.geometric.DotProductShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.geometric.LengthShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.geometric.NormalizeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.trigonometry.ArccosShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.trigonometry.ArcsinShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.trigonometry.ArctanShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.trigonometry.CosShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.trigonometry.DegreesShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.trigonometry.RadiansShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.trigonometry.SinShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.trigonometry.TanShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.value.MergeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.value.RemapShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.math.value.SplitShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.noise.SimplexNoise2DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.noise.SimplexNoise3DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.noise.VoronoiDistanceNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.provided.InstanceIdShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.config.provided.TimeShaderNodeConfiguration;
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
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ExponentialShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ExponentialBase2ShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new NaturalLogarithmShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new LogarithmBase2ShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new SquareRootShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new InverseSquareRootShaderNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new SinShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new CosShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new TanShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ArcsinShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ArccosShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ArctanShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new RadiansShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new DegreesShaderNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new AbsShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new SignShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new FloorShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new CeilingShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new FractionalPartShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ModuloShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new MinimumShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new MaximumShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new ClampShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new LerpShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new StepShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new SmoothstepShaderNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new LengthShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new DistanceShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new DotProductShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new CrossProductShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new NormalizeShaderNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new SplitShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new MergeShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new RemapShaderNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new IntensityShaderNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new SimplexNoise2DNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new SimplexNoise3DNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new VoronoiDistanceNodeConfiguration()));

        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new TimeShaderNodeConfiguration()));
        graphBoxProducers.add(new GraphBoxProducerImpl<ShaderFieldType>(new InstanceIdShaderNodeConfiguration()));

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
