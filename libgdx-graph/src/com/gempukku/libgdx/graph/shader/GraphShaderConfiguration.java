package com.gempukku.libgdx.graph.shader;

import com.gempukku.libgdx.graph.shader.node.EndGraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.value.ValueBooleanShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.value.ValueColorShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.value.ValueFloatShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.value.ValueVector2ShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.value.ValueVector3ShaderNodeBuilder;

import java.util.HashMap;
import java.util.Map;

public class GraphShaderConfiguration {
    public static Map<String, GraphShaderNodeBuilder> graphShaderNodeBuilders = new HashMap<>();

    static {
        addGraphShaderNodeBuilder(new EndGraphShaderNodeBuilder());

        addGraphShaderNodeBuilder(new ValueBooleanShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ValueColorShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ValueFloatShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ValueVector2ShaderNodeBuilder());
        addGraphShaderNodeBuilder(new ValueVector3ShaderNodeBuilder());
    }

    private static void addGraphShaderNodeBuilder(GraphShaderNodeBuilder builder) {
        graphShaderNodeBuilders.put(builder.getType(), builder);
    }

    private GraphShaderConfiguration() {

    }
}
