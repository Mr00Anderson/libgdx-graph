package com.gempukku.libgdx.graph.ui.pipeline;

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
import com.gempukku.libgdx.graph.PipelineConfiguration;
import com.gempukku.libgdx.graph.pipeline.PipelineSerializer;
import com.gempukku.libgdx.graph.ui.AwareTab;
import com.gempukku.libgdx.graph.ui.UIPipelineLoaderCallback;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedListener;
import com.gempukku.libgdx.graph.ui.graph.GraphConnection;
import com.gempukku.libgdx.graph.ui.graph.GraphContainer;
import com.gempukku.libgdx.graph.ui.graph.PopupMenuProducer;
import com.gempukku.libgdx.graph.ui.graph.PropertyProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PipelineDesignTab extends AwareTab {
    private List<PropertyBox> propertyBoxes = new LinkedList<>();

    private final VerticalGroup pipelineProperties;
    private final GraphContainer graphContainer;

    private Table contentTable;
    private Skin skin;

    public PipelineDesignTab(Skin skin, FileHandle source) {
        super(true, false);

        this.skin = skin;
        contentTable = new Table(skin);

        pipelineProperties = createPropertiesUI(skin);

        Table leftTable = new Table();

        ScrollPane propertiesScroll = new ScrollPane(pipelineProperties, skin);
        propertiesScroll.setFadeScrollBars(false);

        leftTable.add(propertiesScroll).grow().row();
        HorizontalGroup buttons = new HorizontalGroup();
        buttons.align(Align.left);
        leftTable.add(buttons).growX().row();

        graphContainer = new GraphContainer(createPopupMenuProducer());
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
            InputStream stream = source.read();
            try {
                PipelineSerializer.loadPipeline(stream, new UIPipelineLoaderCallback(skin, this, graphContainer));
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

                for (Map.Entry<String, Map<String, GraphBoxProducer>> producersEntry : PipelineConfiguration.graphBoxProducers.entrySet()) {
                    String menuName = producersEntry.getKey();
                    Map<String, GraphBoxProducer> producer = producersEntry.getValue();
                    createSubMenu(popupX, popupY, popupMenu, menuName, producer);
                }

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
            if (!PipelineConfiguration.notAddableProducers.contains(value)) {
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
        for (Map.Entry<String, PropertyBoxProducer> propertyEntry : PipelineConfiguration.propertyProducers.entrySet()) {
            final String name = propertyEntry.getKey();
            final PropertyBoxProducer value = propertyEntry.getValue();
            MenuItem valueMenuItem = new MenuItem(name);
            valueMenuItem.addListener(
                    new ClickListener(Input.Buttons.LEFT) {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            PropertyBox defaultPropertyBox = value.createDefaultPropertyBox(skin);
                            addPropertyBox(skin, name, defaultPropertyBox);
                        }
                    });
            menu.addItem(valueMenuItem);
        }

        return menu;
    }

    public void addPropertyBox(Skin skin, String type, final PropertyBox propertyBox) {
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

    public JSONObject serializePipeline() {
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
            JSONObject property = new JSONObject();
            property.put("name", propertyBox.getName());
            property.put("type", propertyBox.getType());

            JSONObject data = propertyBox.serializeData();
            if (data != null)
                property.put("data", data);

            properties.add(property);
        }
        pipeline.put("properties", properties);

        return pipeline;
    }

    @Override
    public void resized(int width, int height) {
        graphContainer.resize(width, height);
    }
}
