package com.gempukku.libgdx.graph.shader.node.math;

import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.math.MultiplyShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class MultiplyShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public MultiplyShaderNodeBuilder() {
        super(new MultiplyShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildNode(String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext) {
        FieldOutput aValue = inputs.get("a");
        FieldOutput bValue = inputs.get("b");
        ShaderFieldType resultType = determineOutputType(aValue, bValue);

        fragmentShaderBuilder.addMainLine("// Multiply node");
        String name = "result_" + nodeId;
        fragmentShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = " + aValue.getRepresentation() + " * " + bValue.getRepresentation() + ";");

        return Collections.singletonMap("output", new DefaultFieldOutput(resultType, name));
    }

    private ShaderFieldType determineOutputType(FieldOutput a, FieldOutput b) {
        ShaderFieldType aType = a.getFieldType();
        ShaderFieldType bType = b.getFieldType();
        if (aType == ShaderFieldType.Float)
            return bType;
        if (bType == ShaderFieldType.Float)
            return aType;
        if (aType != bType)
            throw new IllegalStateException("Invalid mix of input field types");
        return aType;
    }
}
