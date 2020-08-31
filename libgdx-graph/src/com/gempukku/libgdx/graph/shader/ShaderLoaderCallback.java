package com.gempukku.libgdx.graph.shader;

import com.gempukku.libgdx.graph.GraphDataLoaderCallback;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.GraphValidator;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import org.json.simple.JSONObject;

public class ShaderLoaderCallback extends GraphDataLoaderCallback<GraphShader, ShaderFieldType> {
    @Override
    public void start() {

    }

    @Override
    public GraphShader end() {
        GraphValidator<GraphNode<ShaderFieldType>, GraphConnection, GraphProperty<ShaderFieldType>, ShaderFieldType> graphValidator = new GraphValidator<>();
        GraphValidator.ValidationResult<GraphNode<ShaderFieldType>, GraphConnection, GraphProperty<ShaderFieldType>, ShaderFieldType> result = graphValidator.validateGraph(this, "end");
        if (result.hasErrors())
            throw new IllegalStateException("The graph contains errors, open it in the graph designer and correct them");

        return GraphShaderBuilder.buildShader(this);
    }

    @Override
    protected ShaderFieldType getFieldType(String type) {
        return ShaderFieldType.valueOf(type);
    }

    @Override
    protected NodeConfiguration<ShaderFieldType> getNodeConfiguration(String type, JSONObject data) {
        return GraphShaderConfiguration.graphShaderNodeBuilders.get(type).getConfiguration(data);
    }
}
