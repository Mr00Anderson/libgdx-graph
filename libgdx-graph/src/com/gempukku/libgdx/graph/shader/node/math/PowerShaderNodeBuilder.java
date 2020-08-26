package com.gempukku.libgdx.graph.shader.node.math;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.math.PowerShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class PowerShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public PowerShaderNodeBuilder() {
        super(new PowerShaderNodeConfiguration());
    }

    @Override
    public Map<String, ? extends FieldOutput> buildNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput aValue = inputs.get("a");
        FieldOutput bValue = inputs.get("b");

        fragmentShaderBuilder.addMainLine("// Power node");
        String name = "result_" + nodeId;
        fragmentShaderBuilder.addMainLine("float  " + name + " = pow(" + aValue.getRepresentation() + ", " + bValue.getRepresentation() + ");");

        return Collections.singletonMap("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
