package com.gempukku.libgdx.graph.shader.builder;

import com.gempukku.libgdx.graph.shader.UniformRegistry;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class CommonShaderBuilder {
    protected UniformRegistry uniformRegistry;

    private Map<String, UniformVariable> uniformVariables = new LinkedHashMap<String, UniformVariable>();
    private Map<String, String> varyingVariables = new LinkedHashMap<String, String>();
    private Map<String, String> variables = new LinkedHashMap<String, String>();
    private Map<String, String> functions = new LinkedHashMap<String, String>();
    private Map<String, String> structures = new LinkedHashMap<String, String>();
    private List<String> mainLines = new LinkedList<>();
    private List<String> initialLines = new LinkedList<>();

    public CommonShaderBuilder(UniformRegistry uniformRegistry) {
        this.uniformRegistry = uniformRegistry;
    }

    public void addStructArrayUniformVariable(String name, String[] fieldNames, int size, String type, boolean global,
                                              UniformRegistry.StructArrayUniformSetter setter) {
        addStructArrayUniformVariable(name, fieldNames, size, type, global, setter, null);
    }

    public void addStructArrayUniformVariable(String name, String[] fieldNames, int size, String type, boolean global,
                                              UniformRegistry.StructArrayUniformSetter setter, String comment) {
        if (uniformVariables.containsKey(name))
            throw new IllegalStateException("Already contains uniform of that name");
        uniformRegistry.registerStructArrayUniform(name, fieldNames, global, setter);
        uniformVariables.put(name + "[" + size + "]", new UniformVariable(type, global, null, comment));
    }

    public void addArrayUniformVariable(String name, int size, String type, boolean global, UniformRegistry.UniformSetter setter) {
        addArrayUniformVariable(name, size, type, global, setter, null);
    }

    public void addArrayUniformVariable(String name, int size, String type, boolean global, UniformRegistry.UniformSetter setter, String comment) {
        UniformVariable uniformVariable = uniformVariables.get(name);
        if (uniformVariable != null &&
                (!uniformVariable.type.equals(type) || uniformVariable.global != global || uniformVariable.size != size))
            throw new IllegalStateException("Already contains uniform of that name with a different type, size or global flag");

        if (uniformVariable == null) {
            uniformRegistry.registerUniform(name, global, setter);
            uniformVariables.put(name, new UniformVariable(type, global, setter, comment, size));
        }
    }

    public boolean hasUniformVariable(String name) {
        return uniformVariables.containsKey(name);
    }

    public void addUniformVariable(String name, String type, boolean global, UniformRegistry.UniformSetter setter) {
        addUniformVariable(name, type, global, setter, null);
    }

    public void addUniformVariable(String name, String type, boolean global, UniformRegistry.UniformSetter setter, String comment) {
        addArrayUniformVariable(name, -1, type, global, setter, comment);
    }

    public boolean hasVaryingVariable(String name) {
        return varyingVariables.containsKey(name);
    }

    public void addVaryingVariable(String name, String type) {
        if (varyingVariables.containsKey(name))
            throw new IllegalStateException("Already contains varying variable of that name");
        varyingVariables.put(name, type);
    }

    public void addVariable(String name, String type) {
        if (variables.containsKey(name))
            throw new IllegalStateException("Already contains variable of that name");
        variables.put(name, type);
    }

    public void addFunction(String name, String functionText) {
        if (functions.containsKey(name))
            throw new IllegalStateException("Already contains function of that name");
        functions.put(name, functionText);
    }

    public boolean containsFunction(String name) {
        return functions.containsKey(name);
    }

    public void addStructure(String name, String structureText) {
        if (structures.containsKey(name) && !structures.get(name).equals(structureText))
            throw new IllegalStateException("Already contains structure of that name with different text");
        structures.put(name, structureText);
    }

    public void addInitialLine(String initialLine) {
        initialLines.add(initialLine);
    }

    public void addMainLine(String mainLine) {
        mainLines.add(mainLine);
    }

    protected void appendUniformVariables(StringBuilder stringBuilder) {
        for (Map.Entry<String, UniformVariable> uniformDefinition : uniformVariables.entrySet()) {
            UniformVariable uniformVariable = uniformDefinition.getValue();
            String name = uniformDefinition.getKey();
            if (uniformVariable.size > -1)
                name += "[" + uniformVariable.size + "]";
            if (uniformVariable.comment != null)
                stringBuilder.append("// " + uniformVariable.comment + "\n");
            stringBuilder.append("uniform " + uniformVariable.type + " " + name + ";\n");
        }
        if (!uniformVariables.isEmpty())
            stringBuilder.append('\n');
    }

    protected void appendVaryingVariables(StringBuilder stringBuilder) {
        for (Map.Entry<String, String> varyingDefinition : varyingVariables.entrySet()) {
            stringBuilder.append("varying " + varyingDefinition.getValue() + " " + varyingDefinition.getKey() + ";\n");
        }
        if (!varyingVariables.isEmpty())
            stringBuilder.append('\n');
    }

    protected void appendVariables(StringBuilder stringBuilder) {
        for (Map.Entry<String, String> variable : variables.entrySet()) {
            stringBuilder.append(variable.getValue() + " " + variable.getKey() + ";\n");
        }
        if (!variables.isEmpty())
            stringBuilder.append('\n');
    }

    protected void appendFunctions(StringBuilder stringBuilder) {
        for (String function : functions.values()) {
            stringBuilder.append(function);
            stringBuilder.append('\n');
        }
    }

    protected void appendStructures(StringBuilder stringBuilder) {
        for (Map.Entry<String, String> structureEntry : structures.entrySet()) {
            stringBuilder.append("struct " + structureEntry.getKey() + " {\n")
                    .append(structureEntry.getValue()).append("};\n\n");
        }
        if (!structures.isEmpty())
            stringBuilder.append('\n');
    }

    protected void appendInitial(StringBuilder stringBuilder) {
        for (String initialLine : initialLines) {
            stringBuilder.append(initialLine).append('\n');
        }
        if (!initialLines.isEmpty())
            stringBuilder.append('\n');
    }

    protected void appendMain(StringBuilder stringBuilder) {
        stringBuilder.append("void main() {\n");
        for (String mainLine : mainLines) {
            stringBuilder.append("  ").append(mainLine).append('\n');
        }

        stringBuilder.append("}\n");
    }

    private class UniformVariable {
        public final String type;
        public final int size;
        public final boolean global;
        public final UniformRegistry.UniformSetter setter;
        public final String comment;

        public UniformVariable(String type, boolean global, UniformRegistry.UniformSetter setter, String comment) {
            this(type, global, setter, comment, -1);
        }

        public UniformVariable(String type, boolean global, UniformRegistry.UniformSetter setter, String comment, int size) {
            this.type = type;
            this.global = global;
            this.setter = setter;
            this.comment = comment;
            this.size = size;
        }
    }
}
