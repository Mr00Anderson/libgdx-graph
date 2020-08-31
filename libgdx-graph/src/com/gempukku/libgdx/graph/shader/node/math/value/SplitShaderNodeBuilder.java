package com.gempukku.libgdx.graph.shader.node.math.value;

import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.GraphShaderContext;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.math.value.SplitShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SplitShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public SplitShaderNodeBuilder() {
        super(new SplitShaderNodeConfiguration());
    }

    @Override
    protected Map<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JSONObject data, Map<String, FieldOutput> inputs, Set<String> producedOutputs,
                                                                 CommonShaderBuilder commonShaderBuilder, GraphShaderContext graphShaderContext, GraphShader graphShader) {
        FieldOutput inputValue = inputs.get("input");
        ShaderFieldType fieldType = inputValue.getFieldType();

        String x, y, z, w;
        if (fieldType == ShaderFieldType.Color) {
            x = inputValue.getRepresentation() + ".r";
            y = inputValue.getRepresentation() + ".g";
            z = inputValue.getRepresentation() + ".b";
            w = inputValue.getRepresentation() + ".a";
        } else if (fieldType == ShaderFieldType.Vector3) {
            x = inputValue.getRepresentation() + ".x";
            y = inputValue.getRepresentation() + ".y";
            z = inputValue.getRepresentation() + ".z";
            w = "0.0";
        } else if (fieldType == ShaderFieldType.Vector2) {
            x = inputValue.getRepresentation() + ".x";
            y = inputValue.getRepresentation() + ".y";
            z = "0.0";
            w = "0.0";
        } else {
            throw new UnsupportedOperationException();
        }

        commonShaderBuilder.addMainLine("// Split Node");

        Map<String, DefaultFieldOutput> result = new HashMap<>();
        if (producedOutputs.contains("x")) {
            String name = "x_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + x + ";");
            result.put("x", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("y")) {
            String name = "y_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + y + ";");
            result.put("y", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("z")) {
            String name = "z_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + z + ";");
            result.put("z", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        if (producedOutputs.contains("w")) {
            String name = "w_" + nodeId;
            commonShaderBuilder.addMainLine("float " + name + " = " + w + ";");
            result.put("w", new DefaultFieldOutput(ShaderFieldType.Float, name));
        }
        return result;
    }
}
