package com.gempukku.libgdx.graph.shader.builder;


import com.gempukku.libgdx.graph.shader.UniformRegistry;

public class FragmentShaderBuilder extends CommonShaderBuilder {
    public FragmentShaderBuilder(UniformRegistry uniformRegistry) {
        super(uniformRegistry);
    }

    public String buildProgram() {
        StringBuilder result = new StringBuilder();

        appendInitial(result);
        appendStructures(result);
        appendUniformVariables(result);
        appendVaryingVariables(result);
        appendVariables(result);

        appendFunctions(result);

        appendMain(result);

        return result.toString();
    }
}
