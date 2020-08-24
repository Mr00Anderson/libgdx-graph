package com.gempukku.libgdx.graph.shader.node.math;

import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.math.LerpShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class LerpShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public LerpShaderNodeBuilder() {
        super(new LerpShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildNode(String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext) {
        FieldOutput aValue = inputs.get("a");
        FieldOutput bValue = inputs.get("b");
        FieldOutput tValue = inputs.get("t");
        ShaderFieldType resultType = determineOutputType(aValue, bValue, tValue);

        String name = "result_" + nodeId;
        fragmentShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = mix(" + aValue.getRepresentation() + ", " + bValue.getRepresentation() + ", " + tValue.getRepresentation() + ");");

        return Collections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }

    private ShaderFieldType determineOutputType(FieldOutput a, FieldOutput b, FieldOutput t) {
        ShaderFieldType aType = a.getFieldType();
        ShaderFieldType bType = b.getFieldType();
        ShaderFieldType tType = t.getFieldType();
        if (aType != bType || !(tType == ShaderFieldType.Float || tType == aType))
            throw new IllegalStateException("Invalid mix of input field types");
        return aType;
    }
}
