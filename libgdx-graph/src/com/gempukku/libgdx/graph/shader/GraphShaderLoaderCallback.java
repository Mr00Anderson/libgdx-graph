package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.gempukku.libgdx.graph.GraphLoaderCallback;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.property.GraphShaderPropertyProducer;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphShaderLoaderCallback implements GraphLoaderCallback<GraphShader> {
    private Map<String, ShaderNodeInfo> nodes = new HashMap<>();
    private List<ShaderVertextInfo> vertices = new LinkedList<>();
    private Map<String, PropertySource> propertyMap = new HashMap<>();

    private String tag;
    private GraphShader graphShader;

    public GraphShaderLoaderCallback(String tag) {
        this.tag = tag;
    }

    @Override
    public void start() {
        graphShader = new GraphShader(tag);
    }

    @Override
    public void addPipelineNode(String id, String type, float x, float y, JSONObject data) {
        nodes.put(id, new ShaderNodeInfo(id, type, data));
    }

    @Override
    public void addPipelineVertex(String fromNode, String fromField, String toNode, String toField) {
        vertices.add(new ShaderVertextInfo(fromNode, fromField, toNode, toField));
    }

    @Override
    public void addPipelineProperty(String type, String name, JSONObject data) {
        GraphShaderPropertyProducer propertyProducer = findPropertyProducerByType(type);
        if (propertyProducer == null)
            throw new IllegalStateException("Unable to find property producer for type: " + type);
        propertyMap.put(name, propertyProducer.createProperty(name, data));
    }

    @Override
    public GraphShader end() {
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
            ShaderNodeInfo nodeInfo = nodes.get(nodeId);
            GraphShaderNodeBuilder nodeBuilder = GraphShaderConfiguration.graphShaderNodeBuilders.get(nodeInfo.type);
            if (nodeBuilder == null)
                throw new IllegalStateException("Unable to find graph shader node builder for type: " + nodeInfo.type);
            Map<String, GraphShaderNodeBuilder.FieldOutput> inputFields = new HashMap<>();
            for (GraphNodeInput<ShaderFieldType> nodeInput : nodeBuilder.getConfiguration(nodeInfo.data).getNodeInputs().values()) {
                String fieldId = nodeInput.getFieldId();
                ShaderVertextInfo vertexInfo = findInputVertex(nodeId, fieldId);
                if (vertexInfo == null && nodeInput.isRequired())
                    throw new IllegalStateException("Required input not provided");
                if (vertexInfo != null) {
                    Map<String, ? extends GraphShaderNodeBuilder.FieldOutput> output = buildNode(context, vertexInfo.fromNode, nodeOutputs, vertexShaderBuilder, fragmentShaderBuilder);
                    GraphShaderNodeBuilder.FieldOutput fieldOutput = output.get(vertexInfo.fromField);
                    ShaderFieldType fieldType = fieldOutput.getFieldType();
                    if (!nodeInput.getAcceptedPropertyTypes().contains(fieldType))
                        throw new IllegalStateException("Producer produces a field of value not compatible with consumer");
                    inputFields.put(fieldId, fieldOutput);
                }
            }
            Set<String> requiredOutputs = findRequiredOutputs(nodeId);
            nodeOutput = (Map<String, GraphShaderNodeBuilder.FieldOutput>) nodeBuilder.buildNode(nodeId, nodeInfo.data, inputFields, requiredOutputs, vertexShaderBuilder, fragmentShaderBuilder, context);
            nodeOutputs.put(nodeId, nodeOutput);
        }

        return nodeOutput;
    }

    private Set<String> findRequiredOutputs(String nodeId) {
        Set<String> result = new HashSet<>();
        for (ShaderVertextInfo vertex : vertices) {
            if (vertex.fromNode.equals(nodeId))
                result.add(vertex.fromField);
        }
        return result;
    }

    private ShaderVertextInfo findInputVertex(String nodeId, String nodeField) {
        for (ShaderVertextInfo vertex : vertices) {
            if (vertex.toNode.equals(nodeId) && vertex.toField.equals(nodeField))
                return vertex;
        }
        return null;
    }

    private GraphShaderPropertyProducer findPropertyProducerByType(String type) {
        for (GraphShaderPropertyProducer graphShaderPropertyProducer : GraphShaderConfiguration.graphShaderPropertyProducers) {
            if (graphShaderPropertyProducer.supportsType(type))
                return graphShaderPropertyProducer;
        }
        return null;
    }

    private class ShaderNodeInfo {
        private String id;
        private String type;
        private JSONObject data;

        public ShaderNodeInfo(String id, String type, JSONObject data) {
            this.id = id;
            this.type = type;
            this.data = data;
        }
    }

    private class ShaderVertextInfo {
        private String fromNode;
        private String fromField;
        private String toNode;
        private String toField;

        public ShaderVertextInfo(String fromNode, String fromField, String toNode, String toField) {
            this.fromNode = fromNode;
            this.fromField = fromField;
            this.toNode = toNode;
            this.toField = toField;
        }
    }
}
