package com.gempukku.libgdx.graph.pipeline.loader.property;

import com.gempukku.libgdx.graph.PropertyNodeConfiguration;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.loader.PipelineRenderingContext;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNode;
import com.gempukku.libgdx.graph.pipeline.loader.node.PipelineNodeProducer;
import org.json.simple.JSONObject;

import java.util.Map;

public class PropertyPipelineNodeProducer implements PipelineNodeProducer {
    @Override
    public String getType() {
        return "Property";
    }

    @Override
    public NodeConfiguration<PipelineFieldType> getConfiguration(JSONObject data) {
        final String name = (String) data.get("name");
        final PipelineFieldType fieldType = PipelineFieldType.valueOf((String) data.get("type"));
        return new PropertyNodeConfiguration<PipelineFieldType>(name, fieldType);
    }

    @Override
    public PipelineNode createNode(JSONObject data, Map<String, PipelineNode.FieldOutput<?>> inputFields) {
        final String propertyName = (String) data.get("name");
        final PipelineFieldType fieldType = PipelineFieldType.valueOf((String) data.get("type"));

        return new PipelineNode() {
            @Override
            public FieldOutput<?> getFieldOutput(String name) {
                return new FieldOutput<Object>() {
                    @Override
                    public PipelineFieldType getPropertyType() {
                        return fieldType;
                    }

                    @Override
                    public Object getValue(PipelineRenderingContext context) {
                        return context.getPipelinePropertySource().getPipelineProperty(propertyName).getValue();
                    }
                };
            }

            @Override
            public void startFrame(float delta) {

            }

            @Override
            public void endFrame() {

            }

            @Override
            public void dispose() {

            }
        };
    }
}
