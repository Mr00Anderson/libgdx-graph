package com.gempukku.libgdx.graph.ui.pipeline;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.gempukku.graph.pipeline.PipelineSerializer;
import com.gempukku.graph.pipeline.producer.GraphBoxProducer;
import com.gempukku.graph.pipeline.producer.part.MergeBoxProducer;
import com.gempukku.graph.pipeline.producer.part.SplitBoxProducer;
import com.gempukku.graph.pipeline.producer.provided.ScreenSizeBoxProducer;
import com.gempukku.graph.pipeline.producer.provided.TimeBoxProducer;
import com.gempukku.graph.pipeline.producer.value.ValueBooleanBoxProducer;
import com.gempukku.graph.pipeline.producer.value.ValueColorBoxProducer;
import com.gempukku.graph.pipeline.producer.value.ValueVector1BoxProducer;
import com.gempukku.graph.pipeline.producer.value.ValueVector2BoxProducer;
import com.gempukku.graph.pipeline.producer.value.ValueVector3BoxProducer;
import com.gempukku.libgdx.graph.ui.SleepingTab;
import com.gempukku.libgdx.graph.ui.UIPipelineLoaderCallback;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedListener;
import com.gempukku.libgdx.graph.ui.graph.GraphConnection;
import com.gempukku.libgdx.graph.ui.graph.GraphContainer;
import com.gempukku.libgdx.graph.ui.graph.PopupMenuProducer;
import com.gempukku.libgdx.graph.ui.graph.PropertyProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyVector1BoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyVector2BoxProducer;
import com.gempukku.libgdx.graph.ui.pipeline.property.PropertyVector3BoxProducer;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PipelineDesignTab extends SleepingTab {
    private Map<String, GraphBoxProducer> valueProducers = new LinkedHashMap<>();
    private Map<String, GraphBoxProducer> providedProducers = new LinkedHashMap<>();
    private Map<String, GraphBoxProducer> mathProducers = new LinkedHashMap<>();
    private Map<String, PropertyBoxProducer> propertyProducers = new LinkedHashMap<>();

    private List<PropertyBox> propertyBoxes = new LinkedList<>();

    private final VerticalGroup pipelineProperties;
    private final GraphContainer graphContainer;

    private Table contentTable;
    private Skin skin;

    public PipelineDesignTab(Skin skin) {
        super(true, false);

        valueProducers.put("Color", new ValueColorBoxProducer());
        valueProducers.put("Vector1", new ValueVector1BoxProducer());
        valueProducers.put("Vector2", new ValueVector2BoxProducer());
        valueProducers.put("Vector3", new ValueVector3BoxProducer());
        valueProducers.put("Boolean", new ValueBooleanBoxProducer());

        providedProducers.put("Time", new TimeBoxProducer());
        providedProducers.put("Screen Size", new ScreenSizeBoxProducer());

        mathProducers.put("Split", new SplitBoxProducer());
        mathProducers.put("Merge", new MergeBoxProducer());

        propertyProducers.put("Vector1", new PropertyVector1BoxProducer());
        propertyProducers.put("Vector2", new PropertyVector2BoxProducer());
        propertyProducers.put("Vector3", new PropertyVector3BoxProducer());

        this.skin = skin;
        contentTable = new Table(skin);

        pipelineProperties = createPropertiesUI(skin);

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

        graphContainer = new GraphContainer(skin, createPopupMenuProducer());
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

    private PopupMenuProducer createPopupMenuProducer() {
        return new PopupMenuProducer() {
            @Override
            public PopupMenu createPopupMenu(final float popupX, final float popupY) {
                PopupMenu popupMenu = new PopupMenu();

                createSubMenu(popupX, popupY, popupMenu, "Values", valueProducers);
                createSubMenu(popupX, popupY, popupMenu, "Provided", providedProducers);
                createSubMenu(popupX, popupY, popupMenu, "Math", mathProducers);

                popupMenu.addSeparator();
                for (final PropertyProducer propertyProducer : propertyBoxes) {
                    final String name = propertyProducer.getName();
                    MenuItem valueMenuItem = new MenuItem(name);
                    valueMenuItem.addListener(
                            new ClickListener(Input.Buttons.LEFT) {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    String id = UUID.randomUUID().toString().replace("-", "");
                                    GraphBox graphBox = propertyProducer.createPropertyBox(skin, id, popupX, popupY);
                                    graphContainer.addGraphBox(graphBox, name, true, popupX, popupY);
                                }
                            });
                    popupMenu.addItem(valueMenuItem);
                }

                return popupMenu;
            }
        };
    }

    private void createSubMenu(final float popupX, final float popupY, PopupMenu popupMenu, String menuName, Map<String, GraphBoxProducer> producerMap) {
        MenuItem valuesMenuItem = new MenuItem(menuName);
        PopupMenu valuesMenu = new PopupMenu();
        for (Map.Entry<String, GraphBoxProducer> valueEntry : producerMap.entrySet()) {
            final String name = valueEntry.getKey();
            final GraphBoxProducer value = valueEntry.getValue();
            MenuItem valueMenuItem = new MenuItem(name);
            valueMenuItem.addListener(
                    new ClickListener(Input.Buttons.LEFT) {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            String id = UUID.randomUUID().toString().replace("-", "");
                            GraphBox graphBox = value.createDefault(skin, id);
                            graphContainer.addGraphBox(graphBox, name, true, popupX, popupY);
                        }
                    });
            valuesMenu.addItem(valueMenuItem);
        }
        valuesMenuItem.setSubMenu(valuesMenu);
        popupMenu.addItem(valuesMenuItem);
    }

    private VerticalGroup createPropertiesUI(final Skin skin) {
        final VerticalGroup pipelineProperties = new VerticalGroup();
        pipelineProperties.grow();
        Table headerTable = new Table();
        headerTable.add(new Label("Pipeline properties", skin)).growX();
        final TextButton newPropertyButton = new TextButton("+", skin);
        newPropertyButton.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        PopupMenu popupMenu = createPopupMenu(skin);
                        popupMenu.showMenu(pipelineProperties.getStage(), newPropertyButton);
                    }
                });
        headerTable.add(newPropertyButton);
        headerTable.row();
        pipelineProperties.addActor(headerTable);
        return pipelineProperties;
    }

    private PopupMenu createPopupMenu(final Skin skin) {
        PopupMenu menu = new PopupMenu();
        for (Map.Entry<String, PropertyBoxProducer> propertyEntry : propertyProducers.entrySet()) {
            final String name = propertyEntry.getKey();
            final PropertyBoxProducer value = propertyEntry.getValue();
            MenuItem valueMenuItem = new MenuItem(name);
            valueMenuItem.addListener(
                    new ClickListener(Input.Buttons.LEFT) {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            String id = UUID.randomUUID().toString().replace("-", "");
                            PropertyBox defaultPropertyBox = value.createDefaultPropertyBox(skin, id);
                            addPropertyBox(skin, name, defaultPropertyBox);
                        }
                    });
            menu.addItem(valueMenuItem);
        }

        return menu;
    }

    private void addPropertyBox(Skin skin, String type, final PropertyBox propertyBox) {
        propertyBoxes.add(propertyBox);
        final Actor actor = propertyBox.getActor();

        final Table table = new Table(skin);
        table.add(new Label(type, skin)).growX();
        TextButton removeButton = new TextButton("-", skin);
        removeButton.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        propertyBoxes.remove(propertyBox);
                        pipelineProperties.removeActor(table);
                        pipelineProperties.removeActor(actor);
                    }
                });
        table.add(removeButton);
        table.row();
        pipelineProperties.addActor(table);
        pipelineProperties.addActor(actor);
    }

    @Override
    public void sleep() {

    }

    @Override
    public void awaken() {

    }

    @Override
    public void dispose() {
        graphContainer.dispose();
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
        renderer.put("pipeline", serializePipeline());

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

    private JSONObject serializePipeline() {
        JSONObject pipeline = new JSONObject();

        JSONArray objects = new JSONArray();
        for (Map.Entry<GraphBox, Window> graphBoxWindow : graphContainer.getGraphBoxes()) {
            GraphBox graphBox = graphBoxWindow.getKey();
            Window window = graphBoxWindow.getValue();
            JSONObject object = new JSONObject();
            object.put("id", graphBox.getId());
            object.put("type", graphBox.getType());
            object.put("x", window.getX());
            object.put("y", window.getY());

            JSONObject data = graphBox.serializeData();
            if (data != null)
                object.put("data", data);

            objects.add(object);
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

        JSONArray properties = new JSONArray();
        for (PropertyBox propertyBox : propertyBoxes) {
            properties.add(propertyBox.serializeProperty());
        }
        pipeline.put("properties", properties);

        return pipeline;
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
