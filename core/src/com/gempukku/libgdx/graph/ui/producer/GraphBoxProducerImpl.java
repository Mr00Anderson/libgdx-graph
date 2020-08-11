package com.gempukku.libgdx.graph.ui.producer;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeInput;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeOutput;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import org.json.simple.JSONObject;

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
        GraphBoxImpl start = new GraphBoxImpl(id, configuration, skin);
        Iterator<PipelineNodeInput> inputIterator = configuration.getNodeInputs().iterator();
        Iterator<PipelineNodeOutput> outputIterator = configuration.getNodeOutputs().iterator();
        while (inputIterator.hasNext() || outputIterator.hasNext()) {
            PipelineNodeInput input = null;
            PipelineNodeOutput output = null;
            while (inputIterator.hasNext()) {
                input = inputIterator.next();
                if (input.isMainConnection()) {
                    start.addTopConnector(input);
                    input = null;
                } else {
                    break;
                }
            }
            while (outputIterator.hasNext()) {
                output = outputIterator.next();
                if (output.isMainConnection()) {
                    start.addBottomConnector(output);
                    output = null;
                } else {
                    break;
                }
            }

            if (input != null && output != null) {
                start.addTwoSideGraphPart(skin, input, output);
            } else if (input != null) {
                start.addInputGraphPart(skin, input);
            } else if (output != null) {
                start.addOutputGraphPart(skin, output);
            }
        }

        return start;
    }
}
