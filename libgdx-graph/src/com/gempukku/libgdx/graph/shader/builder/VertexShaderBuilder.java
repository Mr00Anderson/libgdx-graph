package com.gempukku.libgdx.graph.shader.builder;

import com.gempukku.libgdx.graph.shader.UniformRegistry;

import java.util.LinkedHashMap;
import java.util.Map;

public class VertexShaderBuilder extends CommonShaderBuilder {
    private Map<String, String> attributeVariables = new LinkedHashMap<String, String>();

    public VertexShaderBuilder(UniformRegistry uniformRegistry) {
        super(uniformRegistry);
    }

    public void addAttributeVariable(String name, String type) {
        String existingType = attributeVariables.get(name);
        if (existingType != null && !existingType.equals(type))
            throw new IllegalStateException("Already contains vertex attribute of that name with different type");
        if (existingType == null) {
            uniformRegistry.registerAttribute(name);
            attributeVariables.put(name, type);
        }
    }

    private void appendAttributeVariables(StringBuilder stringBuilder) {
        for (Map.Entry<String, String> uniformDefinition : attributeVariables.entrySet()) {
            stringBuilder.append("attribute " + uniformDefinition.getValue() + " " + uniformDefinition.getKey() + ";\n");
        }
        if (!attributeVariables.isEmpty())
            stringBuilder.append("\n");
    }

    public String buildProgram() {
        StringBuilder result = new StringBuilder();

        appendInitial(result);
        appendStructures(result);
        appendAttributeVariables(result);
        appendUniformVariables(result);
        appendVaryingVariables(result);
        appendVariables(result);

        appendFunctions(result);

        appendMain(result);

        return result.toString();
    }
}
