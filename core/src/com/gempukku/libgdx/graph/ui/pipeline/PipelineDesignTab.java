package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.gempukku.graph.pipeline.PipelineSerializer;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.libgdx.graph.ui.SleepingTab;
import com.gempukku.libgdx.graph.ui.UIPipelineLoaderCallback;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedListener;
import com.gempukku.libgdx.graph.ui.graph.GraphConnection;
import com.gempukku.libgdx.graph.ui.graph.GraphContainer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class PipelineDesignTab extends SleepingTab {
    private final VerticalGroup pipelineProperties;
    private final GraphContainer graphContainer;

    private Table contentTable;
    private Skin skin;

    private SplitPane splitPane;

    public PipelineDesignTab(Skin skin) {
        super(true, false);

        this.skin = skin;
        contentTable = new Table(skin);

        pipelineProperties = new VerticalGroup();
        pipelineProperties.addActor(new Label("Pipeline properties", skin));

        Table leftTable = new Table();

        ScrollPane propertiesScroll = new ScrollPane(pipelineProperties, skin);
        propertiesScroll.setFadeScrollBars(false);

        leftTable.add(propertiesScroll).grow().row();
        HorizontalGroup buttons = new HorizontalGroup();
        buttons.align(Align.left);
        buttons.addActor(new TextButton("+", skin));

        TextButton save = new TextButton("Save", skin);
        save.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        save();
                    }
                });
        buttons.addActor(save);

        leftTable.add(buttons).growX().row();

        graphContainer = new GraphContainer(skin);
        graphContainer.addListener(
                new GraphChangedListener() {
                    @Override
                    protected boolean graphChanged(GraphChangedEvent event) {
                        setDirty(true);
                        return true;
                    }
                });

        SplitPane splitPane = new SplitPane(leftTable, graphContainer, false, skin);

        splitPane.setMaxSplitAmount(0.3f);
        try {
            InputStream stream = Gdx.files.internal("template/default-pipeline.json").read();
            try {
                PipelineSerializer.loadPipeline(stream, new UIPipelineLoaderCallback(skin, graphContainer));
            } finally {
                stream.close();
            }
        } catch (IOException exp) {
            throw new RuntimeException("Unable to load default pipeline definition", exp);
        }

        contentTable.add(splitPane).grow().row();
    }

    @Override
    public void sleep() {

    }

    @Override
    public void awaken() {

    }

    @Override
    public String getTabTitle() {
        return "Pipeline";
    }

    @Override
    public Table getContentTable() {
        return contentTable;
    }

    public boolean save() {
        JSONObject renderer = new JSONObject();
        JSONObject pipeline = new JSONObject();
        JSONArray objects = new JSONArray();
        for (GraphBox graphBox : graphContainer.getGraphBoxes()) {
            objects.add(graphBox.serializeGraphBox());
        }

        pipeline.put("objects", objects);
        JSONArray connections = new JSONArray();
        for (GraphConnection connection : graphContainer.getConnections()) {
            JSONObject conn = new JSONObject();
            conn.put("from", connection.getFrom().getOutputConnector().getId());
            conn.put("to", connection.getTo().getInputConnector().getId());
            connections.add(conn);
        }

        pipeline.put("connections", connections);
        renderer.put("pipeline", pipeline);

        try {
            FileHandle local = Gdx.files.local("graph.json");
            OutputStreamWriter out = new OutputStreamWriter(local.write(false));
            try {
                renderer.writeJSONString(out);
                out.flush();
            } finally {
                out.close();
            }
        } catch (IOException exp) {
            exp.printStackTrace();
            return false;
        }

        setDirty(false);

        return true;
    }

    private GraphBoxProducer findProducerByType(String type) throws IOException {
        for (GraphBoxProducer producer : PipelineSerializer.producers) {
            if (producer.supportsType(type))
                return producer;
        }
        throw new IOException("Unable to find pipeline producer for type: " + type);
    }

    public void resize(int width, int height) {
        graphContainer.resize(width, height);
    }
}
