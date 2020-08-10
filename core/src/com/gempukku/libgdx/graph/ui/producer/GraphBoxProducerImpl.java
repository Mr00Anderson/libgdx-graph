package com.gempukku.libgdx.graph.ui.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.renderer.PropertyType;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInput;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutput;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.Iterator;

public class GraphBoxProducerImpl implements GraphBoxProducer {
    private PipelineNodeConfiguration configuration;

    public GraphBoxProducerImpl(PipelineNodeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public boolean isCloseable() {
        return true;
    }

    @Override
    public String getTitle() {
        return configuration.getName();
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, String id, JSONObject data) {
        return createDefault(skin, id);
    }

    @Override
    public GraphBox createDefault(Skin skin, String id) {
        GraphBoxImpl start = new GraphBoxImpl(id, configuration.getType(), skin);
        Iterator<PipelineNodeInput> inputIterator = configuration.getNodeInputs().iterator();
        Iterator<PipelineNodeOutput> outputIterator = configuration.getNodeOutputs().iterator();
        while (inputIterator.hasNext() || outputIterator.hasNext()) {
            PipelineNodeInput input = null;
            PipelineNodeOutput output = null;
            while (inputIterator.hasNext()) {
                input = inputIterator.next();
                if (input.isMainConnection()) {
                    start.addTopConnector(id + ":" + input.getFieldId(), Predicates.in(input.getPropertyType().getAcceptedPropertyTypes()));
                    input = null;
                } else {
                    break;
                }
            }
            while (outputIterator.hasNext()) {
                output = outputIterator.next();
                if (output.isMainConnection()) {
                    final PipelineNodeOutput finalOutput = output;
                    start.addBottomConnector(id + ":" + output.getFieldId(),
                            new Predicate<PropertyType>() {
                                @Override
                                public boolean apply(@Nullable PropertyType propertyType) {
                                    return finalOutput.getPropertyType().mayProduce(propertyType);
                                }
                            });
                    output = null;
                } else {
                    break;
                }
            }

            if (input != null && output != null) {
                final PipelineNodeOutput finalOutput1 = output;
                start.addTwoSideGraphPart(skin,
                        id + ":" + input.getFieldId(), input.getFieldName(), Predicates.in(input.getPropertyType().getAcceptedPropertyTypes()),
                        id + ":" + output.getFieldId(), output.getFieldName(),
                        new Predicate<PropertyType>() {
                            @Override
                            public boolean apply(@Nullable PropertyType propertyType) {
                                return finalOutput1.getPropertyType().mayProduce(propertyType);
                            }
                        });
            } else if (input != null) {
                start.addInputGraphPart(skin, id + ":" + input.getFieldId(), input.getFieldName(), Predicates.in(input.getPropertyType().getAcceptedPropertyTypes()));
            } else if (output != null) {
                final PipelineNodeOutput finalOutput2 = output;
                start.addOutputGraphPart(skin, id + ":" + output.getFieldId(), output.getFieldName(),
                        new Predicate<PropertyType>() {
                            @Override
                            public boolean apply(@Nullable PropertyType propertyType) {
                                return finalOutput2.getPropertyType().mayProduce(propertyType);
                            }
                        });
            }
        }

        return start;
    }
}
