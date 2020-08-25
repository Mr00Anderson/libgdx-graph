package com.gempukku.libgdx.graph.pipeline;

import com.gempukku.libgdx.graph.GraphDataLoaderCallback;
import com.gempukku.libgdx.graph.NodeConfiguration;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphValidator;
import com.gempukku.libgdx.graph.pipeline.impl.PipelineRendererImpl;
import com.gempukku.libgdx.graph.pipeline.impl.WritablePipelineProperty;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducer;
import com.gempukku.libgdx.graph.pipeline.loader.rendering.node.EndPipelineNode;
import com.gempukku.libgdx.graph.pipeline.property.PipelinePropertyProducer;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PipelineLoaderCallback extends GraphDataLoaderCallback<PipelineRenderer, PipelineFieldType> {
    @Override
    public void start() {

    }

    @Override
    public PipelineRenderer end() {
        GraphValidator.ValidationResult<GraphNodeData<PipelineFieldType>, GraphConnectionData, GraphPropertyData<PipelineFieldType>, PipelineFieldType> result = GraphValidator.validateGraph(this, "end");
        if (result.hasErrors())
            throw new IllegalStateException("The graph contains errors, open it in the graph designer and correct them");

        Map<String, PipelineNode> pipelineNodeMap = new HashMap<>();
        PipelineNode pipelineNode = populatePipelineNodes("end", pipelineNodeMap);

        Map<String, WritablePipelineProperty> propertyMap = new HashMap<>();
        for (GraphPropertyData<PipelineFieldType> property : getProperties()) {
            propertyMap.put(property.getName(), findPropertyProducerByType(property.getType()).createProperty(property.getData()));
        }

        return new PipelineRendererImpl(
                pipelineNodeMap.values(), propertyMap, (EndPipelineNode) pipelineNode);
    }

    @Override
    protected PipelineFieldType getFieldType(String type) {
        return PipelineFieldType.valueOf(type);
    }

    @Override
    protected NodeConfiguration<PipelineFieldType> getNodeConfiguration(String type, JSONObject data) {
        return RendererPipelineConfiguration.pipelineNodeProducers.get(type).getConfiguration(data);
    }

    private PipelinePropertyProducer findPropertyProducerByType(PipelineFieldType type) {
        for (PipelinePropertyProducer pipelinePropertyProducer : RendererPipelineConfiguration.pipelinePropertyProducers) {
            if (pipelinePropertyProducer.getType() == type)
                return pipelinePropertyProducer;
        }
        return null;
    }

    private PipelineNode populatePipelineNodes(String nodeId, Map<String, PipelineNode> pipelineNodeMap) {
        PipelineNode pipelineNode = pipelineNodeMap.get(nodeId);
        if (pipelineNode != null)
            return pipelineNode;

        GraphNodeData<PipelineFieldType> nodeInfo = getNodeById(nodeId);
        String nodeInfoType = nodeInfo.getConfiguration().getType();
        PipelineNodeProducer nodeProducer = RendererPipelineConfiguration.pipelineNodeProducers.get(nodeInfoType);
        if (nodeProducer == null)
            throw new IllegalStateException("Unable to find node producer for type: " + nodeInfoType);
        Map<String, PipelineNode.FieldOutput<?>> inputFields = new HashMap<>();
        for (GraphNodeInput<PipelineFieldType> nodeInput : nodeProducer.getConfiguration(nodeInfo.getData()).getNodeInputs().values()) {
            String inputName = nodeInput.getFieldId();
            GraphConnectionData vertexInfo = findInputProducer(nodeId, inputName);
            if (vertexInfo == null && nodeInput.isRequired())
                throw new IllegalStateException("Required input not provided");
            if (vertexInfo != null) {
                PipelineNode vertexNode = populatePipelineNodes(vertexInfo.getNodeFrom(), pipelineNodeMap);
                PipelineNode.FieldOutput<?> fieldOutput = vertexNode.getFieldOutput(vertexInfo.getFieldFrom());
                PipelineFieldType fieldType = fieldOutput.getPropertyType();
                if (!nodeInput.getAcceptedPropertyTypes().contains(fieldType))
                    throw new IllegalStateException("Producer produces a field of value not compatible with consumer");
                inputFields.put(inputName, fieldOutput);
            }
        }
        pipelineNode = nodeProducer.createNode(nodeInfo.getData(), inputFields);
        pipelineNodeMap.put(nodeId, pipelineNode);
        return pipelineNode;
    }

    private GraphConnectionData findInputProducer(String nodeId, String nodeField) {
        for (GraphConnectionData vertex : getConnections()) {
            if (vertex.getNodeTo().equals(nodeId) && vertex.getFieldTo().equals(nodeField))
                return vertex;
        }
        return null;
    }
}
