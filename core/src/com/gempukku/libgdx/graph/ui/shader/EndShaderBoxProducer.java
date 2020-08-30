package com.gempukku.libgdx.graph.ui.shader;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphNode;
import com.gempukku.libgdx.graph.data.GraphNodeInput;
import com.gempukku.libgdx.graph.data.GraphNodeOutput;
import com.gempukku.libgdx.graph.data.GraphProperty;
import com.gempukku.libgdx.graph.data.NodeConfiguration;
import com.gempukku.libgdx.graph.shader.BasicShader;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.config.EndShaderNodeConfiguration;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPart;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.shader.ui.SelectBoxPart;
import com.gempukku.libgdx.graph.ui.shader.ui.ShaderPreviewWidget;
import org.json.simple.JSONObject;

import java.util.Iterator;

public class EndShaderBoxProducer implements GraphBoxProducer<ShaderFieldType> {
    private NodeConfiguration<ShaderFieldType> configuration = new EndShaderNodeConfiguration();

    @Override
    public String getType() {
        return configuration.getType();
    }

    @Override
    public boolean isCloseable() {
        return false;
    }

    @Override
    public String getName() {
        return configuration.getName();
    }

    @Override
    public String getMenuLocation() {
        return configuration.getMenuLocation();
    }

    @Override
    public GraphBox<ShaderFieldType> createPipelineGraphBox(Skin skin, String id, JSONObject data) {
        final ShaderPreviewBoxPart previewBoxPart = new ShaderPreviewBoxPart(skin);
        previewBoxPart.initialize(data);

        GraphBoxImpl<ShaderFieldType> result = new GraphBoxImpl<ShaderFieldType>(id, configuration, skin) {
            @Override
            public void graphChanged(GraphChangedEvent event, boolean hasErrors, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
                if (event.isData() || event.isStructure()) {
                    previewBoxPart.graphChanged(hasErrors, graph);
                }
            }
        };

        addInputsAndOutputs(result, skin);
        SelectBoxPart<ShaderFieldType> cullingBox = new SelectBoxPart<>(skin, "Culling", "culling", BasicShader.Culling.values());
        cullingBox.initialize(data);
        result.addGraphBoxPart(cullingBox);
        SelectBoxPart<ShaderFieldType> transparencyBox = new SelectBoxPart<>(skin, "Transparency", "transparency", BasicShader.Transparency.values());
        transparencyBox.initialize(data);
        result.addGraphBoxPart(transparencyBox);
        SelectBoxPart<ShaderFieldType> blendingBox = new SelectBoxPart<>(skin, "Blending", "blending", BasicShader.Blending.values());
        blendingBox.initialize(data);
        result.addGraphBoxPart(blendingBox);

        result.addGraphBoxPart(previewBoxPart);
        return result;
    }

    @Override
    public GraphBox<ShaderFieldType> createDefault(Skin skin, String id) {
        return createPipelineGraphBox(skin, id, null);
    }

    private void addInputsAndOutputs(GraphBoxImpl<ShaderFieldType> graphBox, Skin skin) {
        Iterator<GraphNodeInput<ShaderFieldType>> inputIterator = configuration.getNodeInputs().values().iterator();
        Iterator<GraphNodeOutput<ShaderFieldType>> outputIterator = configuration.getNodeOutputs().values().iterator();
        while (inputIterator.hasNext() || outputIterator.hasNext()) {
            GraphNodeInput<ShaderFieldType> input = null;
            GraphNodeOutput<ShaderFieldType> output = null;
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

    private static class ShaderPreviewBoxPart extends Table implements GraphBoxPart<ShaderFieldType> {
        private final ShaderPreviewWidget shaderPreviewWidget;

        public ShaderPreviewBoxPart(Skin skin) {
            shaderPreviewWidget = new ShaderPreviewWidget(200, 200);
            add(shaderPreviewWidget).grow().row();
        }

        public void initialize(JSONObject data) {
        }

        @Override
        public Actor getActor() {
            return this;
        }

        @Override
        public GraphBoxOutputConnector<ShaderFieldType> getOutputConnector() {
            return null;
        }

        @Override
        public GraphBoxInputConnector<ShaderFieldType> getInputConnector() {
            return null;
        }

        @Override
        public void serializePart(JSONObject object) {
        }

        public void graphChanged(boolean hasErrors, Graph<? extends GraphNode<ShaderFieldType>, ? extends GraphConnection, ? extends GraphProperty<ShaderFieldType>, ShaderFieldType> graph) {
            shaderPreviewWidget.graphChanged(hasErrors, graph);
        }

        @Override
        public void dispose() {
            shaderPreviewWidget.dispose();
        }
    }
}
