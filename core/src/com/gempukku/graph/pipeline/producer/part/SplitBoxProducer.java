package com.gempukku.graph.pipeline.producer.part;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gempukku.graph.pipeline.PropertyType;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxImpl;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxInputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxOutputConnector;
import com.gempukku.libgdx.graph.ui.graph.GraphBoxPartImpl;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.UUID;

public class SplitBoxProducer implements GraphBoxProducer {
    @Override
    public boolean supportsType(String type) {
        return type.equals("Split");
    }

    @Override
    public GraphBox createPipelineGraphBox(Skin skin, JSONObject jsonObject) {
        String id = (String) jsonObject.get("id");
        float x = ((Number) jsonObject.get("x")).floatValue();
        float y = ((Number) jsonObject.get("y")).floatValue();

        return createGraphBox(skin, id, x, y);
    }

    @Override
    public GraphBox createDefault(Skin skin, float x, float y) {
        String id = UUID.randomUUID().toString().replace("-", "");

        return createGraphBox(skin, id, x, y);
    }

    private GraphBox createGraphBox(Skin skin, String id, float x, float y) {
        GraphBoxImpl end = new GraphBoxImpl(id, "Split", "Split", skin);
        end.setPosition(x, y);
        end.addGraphBoxPart(createTwoPart(skin, id + ":input", id + ":x", "Input", "X",
                Predicates.in(Arrays.asList(PropertyType.Vector2, PropertyType.Vector3, PropertyType.Color)), PropertyType.Vector1));
        end.addGraphBoxPart(createOutputPart(skin, id + ":y", "Y", PropertyType.Vector1));
        end.addGraphBoxPart(createOutputPart(skin, id + ":z", "Z", PropertyType.Vector1));
        end.addGraphBoxPart(createOutputPart(skin, id + ":w", "W", PropertyType.Vector1));

        return end;
    }

    private GraphBoxPartImpl createTwoPart(
            Skin skin,
            String inputId, String outputId,
            String inputText, String outputText,
            Predicate<PropertyType> inputType, PropertyType outputType) {
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new Label(inputText, skin));
        horizontalGroup.addActor(new Label(outputText, skin));

        GraphBoxPartImpl timePart = new GraphBoxPartImpl(horizontalGroup, null);
        timePart.setInputConnector(inputId, GraphBoxInputConnector.Side.Left, inputType);
        timePart.setOutputConnector(outputId, GraphBoxOutputConnector.Side.Right, outputType);
        return timePart;
    }

    private GraphBoxPartImpl createOutputPart(
            Skin skin,
            String id,
            String text,
            PropertyType type) {
        HorizontalGroup horizontalGroup = new HorizontalGroup();
        horizontalGroup.addActor(new Label(text, skin));

        GraphBoxPartImpl timePart = new GraphBoxPartImpl(horizontalGroup, null);
        timePart.setOutputConnector(id, GraphBoxOutputConnector.Side.Right, type);
        return timePart;
    }
}
