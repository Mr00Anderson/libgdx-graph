package com.gempukku.libgdx.graph.renderer;

import com.gempukku.libgdx.graph.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.renderer.impl.PipelineRendererImpl;
import com.gempukku.libgdx.graph.renderer.impl.WritablePipelineProperty;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.renderer.loader.rendering.node.EndPipelineNode;
import com.gempukku.libgdx.graph.renderer.property.PipelinePropertyProducer;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RendererLoaderCallback implements PipelineLoaderCallback<PipelineRenderer> {
    private Map<String, PipelineNodeInfo> nodes = new HashMap<>();
    private List<PipelineVertextInfo> vertices = new LinkedList<>();
    private Map<String, WritablePipelineProperty<PipelineFieldType>> propertyMap = new HashMap<>();

    @Override
    public void start() {

    }

    @Override
    public void addPipelineNode(String id, String type, float x, float y, JSONObject data) {
        nodes.put(id, new PipelineNodeInfo(id, type, data));
    }

    @Override
    public void addPipelineVertex(String fromNode, String fromField, String toNode, String toField) {
        vertices.add(new PipelineVertextInfo(fromNode, fromField, toNode, toField));
    }

    @Override
    public void addPipelineProperty(String type, String name, JSONObject data) {
        PipelinePropertyProducer propertyProducer = findPropertyProducerByType(type);
        if (propertyProducer == null)
            throw new IllegalStateException("Unable to find property producer for type: " + type);
        propertyMap.put(name, propertyProducer.createProperty(data));
    }

    @Override
    public PipelineRenderer end() {
        Map<String, PipelineNode> pipelineNodeMap = new HashMap<>();
        PipelineNode pipelineNode = populatePipelineNodes("end", pipelineNodeMap);

        return new PipelineRendererImpl(
                pipelineNodeMap.values(), propertyMap, (EndPipelineNode) pipelineNode);
    }

    private PipelinePropertyProducer<PipelineFieldType> findPropertyProducerByType(String type) {
        for (PipelinePropertyProducer<PipelineFieldType> pipelinePropertyProducer : RendererPipelineConfiguration.pipelinePropertyProducers) {
            if (pipelinePropertyProducer.supportsType(type))
                return pipelinePropertyProducer;
        }
        return null;
    }

    private PipelineNode populatePipelineNodes(String nodeId, Map<String, PipelineNode> pipelineNodeMap) {
        PipelineNode pipelineNode = pipelineNodeMap.get(nodeId);
        if (pipelineNode != null)
            return pipelineNode;

        PipelineNodeInfo nodeInfo = nodes.get(nodeId);
        PipelineNodeProducer nodeProducer = RendererPipelineConfiguration.pipelineNodeProducers.get(nodeInfo.type);
        if (nodeProducer == null)
            throw new IllegalStateException("Unable to find node producer for type: " + nodeInfo.type);
        Map<String, PipelineNode.FieldOutput<?>> inputFields = new HashMap<>();
        for (GraphNodeInput nodeInput : nodeProducer.getConfiguration(nodeInfo.data).getNodeInputs()) {
            String inputName = nodeInput.getFieldId();
            PipelineVertextInfo vertexInfo = findInputProducer(nodeId, inputName);
            if (vertexInfo == null && nodeInput.isRequired())
                throw new IllegalStateException("Required input not provided");
            if (vertexInfo != null) {
                PipelineNode vertexNode = populatePipelineNodes(vertexInfo.fromNode, pipelineNodeMap);
                PipelineNode.FieldOutput<?> fieldOutput = vertexNode.getFieldOutput(vertexInfo.fromField);
                FieldType fieldType = fieldOutput.getPropertyType();
                if (!nodeInput.getAcceptedPropertyTypes().contains(fieldType))
                    throw new IllegalStateException("Producer produces a field of value not compatible with consumer");
                inputFields.put(inputName, fieldOutput);
            }
        }
        pipelineNode = nodeProducer.createNode(nodeInfo.data, inputFields);
        pipelineNodeMap.put(nodeId, pipelineNode);
        return pipelineNode;
    }

    private PipelineVertextInfo findInputProducer(String nodeId, String nodeField) {
        for (PipelineVertextInfo vertex : vertices) {
            if (vertex.toNode.equals(nodeId) && vertex.toField.equals(nodeField))
                return vertex;
        }
        return null;
    }

    private class PipelineNodeInfo {
        private String id;
        private String type;
        private JSONObject data;

        public PipelineNodeInfo(String id, String type, JSONObject data) {
            this.id = id;
            this.type = type;
            this.data = data;
        }
    }

    private class PipelineVertextInfo {
        private String fromNode;
        private String fromField;
        private String toNode;
        private String toField;

        public PipelineVertextInfo(String fromNode, String fromField, String toNode, String toField) {
            this.fromNode = fromNode;
            this.fromField = fromField;
            this.toNode = toNode;
            this.toField = toField;
        }
    }
}
