package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.GraphDataLoaderCallback;
import com.gempukku.libgdx.graph.NodeConfiguration;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphValidator;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShaderLoaderCallback extends GraphDataLoaderCallback<GraphShader, ShaderFieldType> {
    private String tag;
    private GraphShader graphShader;

    public ShaderLoaderCallback(String tag) {
        this.tag = tag;
    }

    @Override
    public void start() {

    }

    @Override
    public GraphShader end() {
        GraphValidator.ValidationResult<GraphNodeData<ShaderFieldType>, GraphConnectionData, GraphPropertyData<ShaderFieldType>, ShaderFieldType> result = GraphValidator.validateGraph(this, "end");
        if (result.hasErrors())
            throw new IllegalStateException("The graph contains errors, open it in the graph designer and correct them");

        graphShader = new GraphShader(tag);

        Map<String, PropertySource> propertyMap = new HashMap<>();
        for (GraphPropertyData<ShaderFieldType> property : getProperties()) {
            String name = property.getName();
            propertyMap.put(name, findPropertyProducerByType(property.getType()).createProperty(name, property.getData()));
        }

        VertexShaderBuilder vertexShaderBuilder = new VertexShaderBuilder(graphShader);
        FragmentShaderBuilder fragmentShaderBuilder = new FragmentShaderBuilder(graphShader);

        initializeShaders(vertexShaderBuilder, fragmentShaderBuilder);

        GraphShaderContext context = new GraphShaderContextImpl(propertyMap);
        buildGraph(context, vertexShaderBuilder, fragmentShaderBuilder);

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
        return graphShader;
    }

    @Override
    protected ShaderFieldType getFieldType(String type) {
        return ShaderFieldType.valueOf(type);
    }

    @Override
    protected NodeConfiguration<ShaderFieldType> getNodeConfiguration(String type, JSONObject data) {
        return GraphShaderConfiguration.graphShaderNodeBuilders.get(type).getConfiguration(data);
    }

    private void initializeShaders(VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
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

    private void buildGraph(GraphShaderContext context, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        Map<String, Map<String, GraphShaderNodeBuilder.FieldOutput>> graphNodeOutputs = new HashMap<>();
        buildNode(context, "end", graphNodeOutputs, vertexShaderBuilder, fragmentShaderBuilder);
    }

    private Map<String, GraphShaderNodeBuilder.FieldOutput> buildNode(GraphShaderContext context, String nodeId, Map<String, Map<String, GraphShaderNodeBuilder.FieldOutput>> nodeOutputs,
                                                                      VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder) {
        Map<String, GraphShaderNodeBuilder.FieldOutput> nodeOutput = nodeOutputs.get(nodeId);
        if (nodeOutput == null) {
            GraphNodeData<ShaderFieldType> nodeInfo = getNodeById(nodeId);
            String nodeInfoType = nodeInfo.getConfiguration().getType();
            GraphShaderNodeBuilder nodeBuilder = GraphShaderConfiguration.graphShaderNodeBuilders.get(nodeInfoType);
            if (nodeBuilder == null)
                throw new IllegalStateException("Unable to find graph shader node builder for type: " + nodeInfoType);
            Map<String, GraphShaderNodeBuilder.FieldOutput> inputFields = new HashMap<>();
            for (GraphNodeInput<ShaderFieldType> nodeInput : nodeBuilder.getConfiguration(nodeInfo.getData()).getNodeInputs().values()) {
                String fieldId = nodeInput.getFieldId();
                GraphConnectionData vertexInfo = findInputVertex(nodeId, fieldId);
                if (vertexInfo == null && nodeInput.isRequired())
                    throw new IllegalStateException("Required input not provided");
                if (vertexInfo != null) {
                    Map<String, ? extends GraphShaderNodeBuilder.FieldOutput> output = buildNode(context, vertexInfo.getNodeFrom(), nodeOutputs, vertexShaderBuilder, fragmentShaderBuilder);
                    GraphShaderNodeBuilder.FieldOutput fieldOutput = output.get(vertexInfo.getFieldFrom());
                    ShaderFieldType fieldType = fieldOutput.getFieldType();
                    if (!nodeInput.getAcceptedPropertyTypes().contains(fieldType))
                        throw new IllegalStateException("Producer produces a field of value not compatible with consumer");
                    inputFields.put(fieldId, fieldOutput);
                }
            }
            Set<String> requiredOutputs = findRequiredOutputs(nodeId);
            nodeOutput = (Map<String, GraphShaderNodeBuilder.FieldOutput>) nodeBuilder.buildNode(nodeId, nodeInfo.getData(), inputFields, requiredOutputs, vertexShaderBuilder, fragmentShaderBuilder, context);
            nodeOutputs.put(nodeId, nodeOutput);
        }

        return nodeOutput;
    }

    private Set<String> findRequiredOutputs(String nodeId) {
        Set<String> result = new HashSet<>();
        for (GraphConnectionData vertex : getConnections()) {
            if (vertex.getNodeFrom().equals(nodeId))
                result.add(vertex.getFieldFrom());
        }
        return result;
    }

    private GraphConnectionData findInputVertex(String nodeId, String nodeField) {
        for (GraphConnectionData vertex : getConnections()) {
            if (vertex.getNodeTo().equals(nodeId) && vertex.getFieldTo().equals(nodeField))
                return vertex;
        }
        return null;
    }

    private GraphShaderPropertyProducer findPropertyProducerByType(ShaderFieldType type) {
        for (GraphShaderPropertyProducer graphShaderPropertyProducer : GraphShaderConfiguration.graphShaderPropertyProducers) {
            if (graphShaderPropertyProducer.getType() == type)
                return graphShaderPropertyProducer;
        }
        return null;
    }
}
