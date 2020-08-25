package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphShaderBuilder {
    public static void buildShader(GraphShader graphShader, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
        buildShader(graphShader, graph, false);
    }

    public static void buildShader(GraphShader graphShader, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                   boolean designTime) {
        Map<String, PropertySource> propertyMap = new HashMap<>();
        for (GraphProperty<ShaderFieldType> property : graph.getProperties()) {
            String name = property.getName();
            propertyMap.put(name, findPropertyProducerByType(property.getType()).createProperty(name, property.getData(), designTime));
        }

        VertexShaderBuilder vertexShaderBuilder = new VertexShaderBuilder(graphShader);
        FragmentShaderBuilder fragmentShaderBuilder = new FragmentShaderBuilder(graphShader);

        initializeShaders(vertexShaderBuilder, fragmentShaderBuilder);

        GraphShaderContext context = new GraphShaderContextImpl(propertyMap);
        buildGraph(designTime, graph, context, vertexShaderBuilder, fragmentShaderBuilder);

        vertexShaderBuilder.addAttributeVariable(ShaderProgram.POSITION_ATTRIBUTE, "vec3");
        vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, UniformSetters.worldTrans);
        vertexShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", false, UniformSetters.projViewTrans);
        vertexShaderBuilder.addMainLine("gl_Position = u_projViewTrans * (u_worldTrans * vec4(a_position, 1.0));");

        String vertexShader = vertexShaderBuilder.buildProgram();
        String fragmentShader = fragmentShaderBuilder.buildProgram();

        System.out.println("--------------");
        System.out.println("Vertex shader:");
        System.out.println(vertexShader);
        System.out.println("----------------");
        System.out.println("Fragment shader:");
        System.out.println(fragmentShader);

        ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
        graphShader.setProgram(shaderProgram);
        graphShader.init();
    }

    private static void initializeShaders(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        vertexShaderBuilder.addInitialLine("#ifdef GL_ES");
        vertexShaderBuilder.addInitialLine("#define LOWP lowp");
        vertexShaderBuilder.addInitialLine("#define MED mediump");
        vertexShaderBuilder.addInitialLine("#define HIGH highp");
        vertexShaderBuilder.addInitialLine("precision mediump float;");
        vertexShaderBuilder.addInitialLine("#else");
        vertexShaderBuilder.addInitialLine("#define MED");
        vertexShaderBuilder.addInitialLine("#define LOWP");
        vertexShaderBuilder.addInitialLine("#define HIGH");
        vertexShaderBuilder.addInitialLine("#endif");

        fragmentShaderBuilder.addInitialLine("#ifdef GL_ES");
        fragmentShaderBuilder.addInitialLine("#define LOWP lowp");
        fragmentShaderBuilder.addInitialLine("#define MED mediump");
        fragmentShaderBuilder.addInitialLine("#define HIGH highp");
        fragmentShaderBuilder.addInitialLine("precision mediump float;");
        fragmentShaderBuilder.addInitialLine("#else");
        fragmentShaderBuilder.addInitialLine("#define MED");
        fragmentShaderBuilder.addInitialLine("#define LOWP");
        fragmentShaderBuilder.addInitialLine("#define HIGH");
        fragmentShaderBuilder.addInitialLine("#endif");
    }

    private static void buildGraph(boolean designTime, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                   GraphShaderContext context, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        Map<String, Map<String, GraphShaderNodeBuilder.FieldOutput>> graphNodeOutputs = new HashMap<>();
        buildNode(designTime, graph, context, "end", graphNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder);
    }

    private static Map<String, GraphShaderNodeBuilder.FieldOutput> buildNode(
            boolean designTime,
            Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
            GraphShaderContext context, String nodeId, Map<String, Map<String, GraphShaderNodeBuilder.FieldOutput>> nodeOutputs,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        Map<String, GraphShaderNodeBuilder.FieldOutput> nodeOutput = nodeOutputs.get(nodeId);
        if (nodeOutput == null) {
            GraphNode<ShaderFieldType> nodeInfo = graph.getNodeById(nodeId);
            String nodeInfoType = nodeInfo.getType();
            GraphShaderNodeBuilder nodeBuilder = GraphShaderConfiguration.graphShaderNodeBuilders.get(nodeInfoType);
            if (nodeBuilder == null)
                throw new IllegalStateException("Unable to find graph shader node builder for type: " + nodeInfoType);
            Map<String, GraphShaderNodeBuilder.FieldOutput> inputFields = new HashMap<>();
            for (GraphNodeInput<ShaderFieldType> nodeInput : nodeBuilder.getConfiguration(nodeInfo.getData()).getNodeInputs().values()) {
                String fieldId = nodeInput.getFieldId();
                GraphConnection vertexInfo = findInputVertex(graph, nodeId, fieldId);
                if (vertexInfo == null && nodeInput.isRequired())
                    throw new IllegalStateException("Required input not provided");
                if (vertexInfo != null) {
                    Map<String, ? extends GraphShaderNodeBuilder.FieldOutput> output = buildNode(designTime, graph, context, vertexInfo.getNodeFrom(), nodeOutputs, vertexShaderBuilder, fragmentShaderBuilder);
                    GraphShaderNodeBuilder.FieldOutput fieldOutput = output.get(vertexInfo.getFieldFrom());
                    ShaderFieldType fieldType = fieldOutput.getFieldType();
                    if (!nodeInput.getAcceptedPropertyTypes().contains(fieldType))
                        throw new IllegalStateException("Producer produces a field of value not compatible with consumer");
                    inputFields.put(fieldId, fieldOutput);
                }
            }
            Set<String> requiredOutputs = findRequiredOutputs(graph, nodeId);
            nodeOutput = (Map<String, GraphShaderNodeBuilder.FieldOutput>) nodeBuilder.buildNode(designTime, nodeId, nodeInfo.getData(), inputFields, requiredOutputs, vertexShaderBuilder, fragmentShaderBuilder, context);
            nodeOutputs.put(nodeId, nodeOutput);
        }

        return nodeOutput;
    }

    private static Set<String> findRequiredOutputs(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                                   String nodeId) {
        Set<String> result = new HashSet<>();
        for (GraphConnection vertex : graph.getConnections()) {
            if (vertex.getNodeFrom().equals(nodeId))
                result.add(vertex.getFieldFrom());
        }
        return result;
    }

    private static GraphConnection findInputVertex(Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                                   String nodeId, String nodeField) {
        for (GraphConnection vertex : graph.getConnections()) {
            if (vertex.getNodeTo().equals(nodeId) && vertex.getFieldTo().equals(nodeField))
                return vertex;
        }
        return null;
    }

    private static GraphShaderPropertyProducer findPropertyProducerByType(ShaderFieldType type) {
        for (GraphShaderPropertyProducer graphShaderPropertyProducer : GraphShaderConfiguration.graphShaderPropertyProducers) {
            if (graphShaderPropertyProducer.getType() == type)
                return graphShaderPropertyProducer;
        }
        return null;
    }
}
