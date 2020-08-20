package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.g3d.Attribute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphShaderAttribute extends Attribute {
    public static final String GraphShaderAlias = "graphShader";
    public static final long GraphShader = register(GraphShaderAlias);

    private Set<String> tags = new HashSet<>();
    private Map<String, Object> variables = new HashMap<>();

    public GraphShaderAttribute() {
        super(GraphShader);
    }

    public void addShaderTag(String tag) {
        tags.add(tag);
    }

    public void removeShaderTag(String tag) {
        tags.remove(tag);
    }

    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    public void removeVariable(String name) {
        variables.remove(name);
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }

    public Object getVariable(String name) {
        return variables.get(name);
    }

    @Override
    public Attribute copy() {
        GraphShaderAttribute result = new GraphShaderAttribute();
        result.tags = new HashSet<>(tags);
        result.variables = new HashMap<>(variables);
        return result;
    }

    @Override
    public int compareTo(Attribute o) {
        if (type != o.type) return (int) (type - o.type);
        GraphShaderAttribute other = (GraphShaderAttribute) o;
        return tags.hashCode() + variables.hashCode() - other.tags.hashCode() - other.variables.hashCode();
    }
}
