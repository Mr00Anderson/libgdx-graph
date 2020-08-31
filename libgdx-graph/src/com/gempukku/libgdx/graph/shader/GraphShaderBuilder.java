package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.TimeProvider;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphShaderBuilder {
    public static GraphShader buildShader(
            Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
        return buildShader(new DefaultTimeKeeper(), graph);
    }

    public static GraphShader buildShader(TimeProvider timeProvider,
                                          Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
        return buildShader(timeProvider, graph, false);
    }

    public static GraphShader buildShader(TimeProvider timeProvider,
                                          Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                          boolean designTime) {
        GraphShader graphShader = new GraphShader();

        Map<String, PropertySource> propertyMap = new HashMap<>();
        for (GraphProperty<ShaderFieldType> property : graph.getProperties()) {
            String name = property.getName();
            propertyMap.put(name, findPropertyProducerByType(property.getType()).createProperty(name, property.getData(), designTime));
        }

        VertexShaderBuilder vertexShaderBuilder = new VertexShaderBuilder(graphShader);
        FragmentShaderBuilder fragmentShaderBuilder = new FragmentShaderBuilder(graphShader);

        initializeShaders(vertexShaderBuilder, fragmentShaderBuilder);

        graphShader.setPropertySourceMap(propertyMap);
        graphShader.setTimeProvider(timeProvider);

        GraphNode<ShaderFieldType> endNode = graph.getNodeById("end");
        JSONObject data = endNode.getData();

        graphShader.setCulling(BasicShader.Culling.valueOf((String) data.get("culling")));
        graphShader.setTransparency(BasicShader.Transparency.valueOf((String) data.get("transparency")));
        graphShader.setBlending(BasicShader.Blending.valueOf((String) data.get("blending")));

        // Vertex part
        Map<String, Map<String, GraphShaderNodeBuilder.FieldOutput>> vertexNodeOutputs = new HashMap<>();
        GraphShaderNodeBuilder.FieldOutput positionField = getOutput(findInputVertex(graph, "end", "position"),
                designTime, false, graph, graphShader, graphShader, vertexNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder);
        String worldPositionField = positionField != null ? positionField.getRepresentation() : "a_position";
        if (positionField != null) {
            vertexShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", false, UniformSetters.projViewTrans);
        } else {
            vertexShaderBuilder.addAttributeVariable(ShaderProgram.POSITION_ATTRIBUTE, "vec3");
            vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, UniformSetters.worldTrans);
            vertexShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", false, UniformSetters.projViewTrans);
        }

        // Fragment part
        Map<String, Map<String, GraphShaderNodeBuilder.FieldOutput>> fragmentNodeOutputs = new HashMap<>();
        GraphShaderNodeBuilder.FieldOutput alphaField = getOutput(findInputVertex(graph, "end", "alpha"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder);
        String alpha = (alphaField != null) ? alphaField.getRepresentation() : "1.0";
        GraphShaderNodeBuilder.FieldOutput alphaClipField = getOutput(findInputVertex(graph, "end", "alphaClip"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder);
        String alphaClip = (alphaClipField != null) ? alphaClipField.getRepresentation() : "0.0";
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("if (" + alpha + " <= " + alphaClip + ")");
        fragmentShaderBuilder.addMainLine("  discard;");

        GraphShaderNodeBuilder.FieldOutput colorField = getOutput(findInputVertex(graph, "end", "color"),
                designTime, true, graph, graphShader, graphShader, fragmentNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder);
        String color;
        if (colorField == null) {
            color = "vec4(1.0, 1.0, 1.0, " + alpha + ")";
        } else if (colorField.getFieldType() == ShaderFieldType.Color) {
            color = "vec4(" + colorField.getRepresentation() + ".rgb, " + alpha + ")";
        } else {
            color = "vec4(" + colorField.getRepresentation() + ", " + alpha + ")";
        }
        fragmentShaderBuilder.addMainLine("// End Graph Node");
        fragmentShaderBuilder.addMainLine("gl_FragColor = " + color + ";");

        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = u_projViewTrans * (u_worldTrans * vec4(" + worldPositionField + ", 1.0));");

        String vertexShader = vertexShaderBuilder.buildProgram();
        String fragmentShader = fragmentShaderBuilder.buildProgram();

        Gdx.app.debug("Shader", "--------------");
        Gdx.app.debug("Shader", "Vertex shader:");
        Gdx.app.debug("Shader", "--------------");
        Gdx.app.debug("Shader", "\n" + vertexShader);
        Gdx.app.debug("Shader", "----------------");
        Gdx.app.debug("Shader", "Fragment shader:");
        Gdx.app.debug("Shader", "----------------");
        Gdx.app.debug("Shader", "\n" + fragmentShader);

        ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
        graphShader.setProgram(shaderProgram);
        graphShader.init();

        return graphShader;
    }

    private static GraphShaderNodeBuilder.FieldOutput getOutput(GraphConnection connection,
                                                                boolean designTime, boolean fragmentShader,
                                                                Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
                                                                GraphShaderContext context, GraphShader graphShader, Map<String, Map<String, GraphShaderNodeBuilder.FieldOutput>> nodeOutputs,
                                                                VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        if (connection == null)
            return null;
        Map<String, ? extends GraphShaderNodeBuilder.FieldOutput> output = buildNode(designTime, fragmentShader, graph, context, graphShader, connection.getNodeFrom(), nodeOutputs, vertexShaderBuilder, fragmentShaderBuilder);
        return output.get(connection.getFieldFrom());
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

    private static Map<String, GraphShaderNodeBuilder.FieldOutput> buildNode(
            boolean designTime, boolean fragmentShader,
            Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph,
            GraphShaderContext context, GraphShader graphShader, String nodeId, Map<String, Map<String, GraphShaderNodeBuilder.FieldOutput>> nodeOutputs,
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
                    Map<String, ? extends GraphShaderNodeBuilder.FieldOutput> output = buildNode(designTime, fragmentShader, graph, context, graphShader, vertexInfo.getNodeFrom(), nodeOutputs, vertexShaderBuilder, fragmentShaderBuilder);
                    GraphShaderNodeBuilder.FieldOutput fieldOutput = output.get(vertexInfo.getFieldFrom());
                    ShaderFieldType fieldType = fieldOutput.getFieldType();
                    if (!nodeInput.getAcceptedPropertyTypes().contains(fieldType))
                        throw new IllegalStateException("Producer produces a field of value not compatible with consumer");
                    inputFields.put(fieldId, fieldOutput);
                }
            }
            Set<String> requiredOutputs = findRequiredOutputs(graph, nodeId);
            if (fragmentShader) {
                nodeOutput = (Map<String, GraphShaderNodeBuilder.FieldOutput>) nodeBuilder.buildFragmentNode(designTime, nodeId, nodeInfo.getData(), inputFields, requiredOutputs, vertexShaderBuilder, fragmentShaderBuilder, context, graphShader);
            } else {
                nodeOutput = (Map<String, GraphShaderNodeBuilder.FieldOutput>) nodeBuilder.buildVertexNode(designTime, nodeId, nodeInfo.getData(), inputFields, requiredOutputs, vertexShaderBuilder, context, graphShader);
            }
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
