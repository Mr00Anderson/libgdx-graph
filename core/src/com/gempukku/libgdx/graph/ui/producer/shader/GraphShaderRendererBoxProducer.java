package com.gempukku.libgdx.graph.ui.producer.shader;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.graph.renderer.PipelineFieldType;
import com.gempukku.libgdx.graph.renderer.config.rendering.GraphShaderRendererPipelineNodeConfiguration;
import com.gempukku.libgdx.graph.renderer.loader.node.PipelineNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import org.json.simple.JSONObject;

import java.util.Iterator;

public class GraphShaderRendererBoxProducer implements GraphBoxProducer<PipelineFieldType> {
    private PipelineNodeConfiguration<PipelineFieldType> configuration = new GraphShaderRendererPipelineNodeConfiguration();

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
    public GraphBox<PipelineFieldType> createPipelineGraphBox(Skin skin, String id, JSONObject data) {
        GraphBoxImpl<PipelineFieldType> result = new GraphBoxImpl<PipelineFieldType>(id, configuration, skin);
        addInputsAndOutputs(result, skin);
        //result.addGraphBoxPart();
        return result;
    }

    private void addInputsAndOutputs(GraphBoxImpl<PipelineFieldType> graphBox, Skin skin) {
        Iterator<GraphNodeInput<PipelineFieldType>> inputIterator = configuration.getNodeInputs().iterator();
        Iterator<GraphNodeOutput<PipelineFieldType>> outputIterator = configuration.getNodeOutputs().iterator();
        while (inputIterator.hasNext() || outputIterator.hasNext()) {
            GraphNodeInput<PipelineFieldType> input = null;
            GraphNodeOutput<PipelineFieldType> output = null;
            while (inputIterator.hasNext()) {
                input = inputIterator.next();
                if (input.isMainConnection()) {
                    graphBox.addTopConnector(input);
                    input = null;
                } else {
                    break;
                }
            }
            while (outputIterator.hasNext()) {
                output = outputIterator.next();
                if (output.isMainConnection()) {
                    graphBox.addBottomConnector(output);
                    output = null;
                } else {
                    break;
                }
            }

            if (input != null && output != null) {
                graphBox.addTwoSideGraphPart(skin, input, output);
            } else if (input != null) {
                graphBox.addInputGraphPart(skin, input);
            } else if (output != null) {
                graphBox.addOutputGraphPart(skin, output);
            }
        }
    }

    @Override
    public GraphBox<PipelineFieldType> createDefault(Skin skin, String id) {
        return null;
    }
}
