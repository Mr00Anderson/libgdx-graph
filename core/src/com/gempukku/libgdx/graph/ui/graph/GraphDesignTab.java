package com.gempukku.libgdx.graph.ui.graph;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
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
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.graph.data.FieldType;
import com.gempukku.libgdx.graph.data.Graph;
import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.GraphValidator;
import com.gempukku.libgdx.graph.ui.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBox;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.preview.PreviewWidget;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class GraphDesignTab<T extends FieldType> extends Tab implements Graph<GraphBox<T>, GraphConnection, PropertyBox<T>, T> {
    private List<PropertyBox<T>> propertyBoxes = new LinkedList<>();
    private final GraphContainer<T> graphContainer;

    private Skin skin;
    private UIGraphConfiguration<T> uiGraphConfiguration;
    private SaveCallback<T> saveCallback;

    private String id;
    private String title;

    private final VerticalGroup pipelineProperties;
    private Table contentTable;
    private Label validationLabel;

    public GraphDesignTab(boolean closeable, String id, String title, Skin skin,
                          UIGraphConfiguration<T> uiGraphConfiguration, SaveCallback<T> saveCallback) {
        super(true, closeable);
        this.id = id;
        this.title = title;

        contentTable = new Table(skin);
        pipelineProperties = createPropertiesUI(skin);
        this.skin = skin;
        this.uiGraphConfiguration = uiGraphConfiguration;
        this.saveCallback = saveCallback;

        graphContainer = new GraphContainer<T>(
                new PopupMenuProducer() {
                    @Override
                    public PopupMenu createPopupMenu(float x, float y) {
                        return createGraphPopupMenu(x, y);
                    }
                });
        contentTable.addListener(
                new GraphChangedListener() {
                    @Override
                    protected boolean graphChanged(GraphChangedEvent event) {
                        setDirty(true);
                        if (event.isStructural())
                            updatePipelineValidation();
                        event.stop();
                        return true;
                    }
                });


        Table leftTable = new Table();

        ScrollPane propertiesScroll = new ScrollPane(pipelineProperties, skin);
        propertiesScroll.setFadeScrollBars(false);

        leftTable.add(propertiesScroll).grow().row();
        PreviewWidget previewWidget = new PreviewWidget(graphContainer);
        leftTable.add(previewWidget).growX().height(200).row();
        HorizontalGroup buttons = new HorizontalGroup();
        TextButton center = new TextButton("Center", skin);
        center.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        graphContainer.centerCanvas();
                        event.stop();
                    }
                });
        buttons.addActor(center);
        validationLabel = new Label("Invalid", skin);
        validationLabel.setColor(Color.RED);
        buttons.addActor(validationLabel);
        buttons.align(Align.left);
        leftTable.add(buttons).growX().row();

        SplitPane splitPane = new SplitPane(leftTable, graphContainer, false, skin);

        splitPane.setMaxSplitAmount(0.2f);

        contentTable.add(splitPane).grow().row();
    }

    @Override
    public PropertyBox<T> getPropertyByName(String name) {
        for (PropertyBox<T> propertyBox : propertyBoxes) {
            if (propertyBox.getName().equals(name))
                return propertyBox;
        }
        return null;
    }

    @Override
    public Iterable<? extends GraphConnection> getConnections() {
        return graphContainer.getConnections();
    }

    @Override
    public Iterable<? extends PropertyBox<T>> getProperties() {
        return propertyBoxes;
    }

    @Override
    public boolean save() {
        super.save();

        if (saveCallback != null)
            saveCallback.save(this);
        setDirty(false);

        return true;
    }

    private PopupMenu createGraphPopupMenu(final float popupX, final float popupY) {
        PopupMenu popupMenu = new PopupMenu();

        for (Map.Entry<String, Set<GraphBoxProducer<T>>> producersEntry : uiGraphConfiguration.getGraphBoxProducers().entrySet()) {
            String menuName = producersEntry.getKey();
            Set<GraphBoxProducer<T>> producer = producersEntry.getValue();
            createSubMenu(popupX, popupY, popupMenu, menuName, producer);
        }

        if (!propertyBoxes.isEmpty()) {
            MenuItem propertyMenuItem = new MenuItem("Property");
            PopupMenu propertyMenu = new PopupMenu();
            for (final PropertyBox<T> propertyProducer : propertyBoxes) {
                final String name = propertyProducer.getName();
                MenuItem valueMenuItem = new MenuItem(name);
                valueMenuItem.addListener(
                        new ClickListener(Input.Buttons.LEFT) {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                String id = UUID.randomUUID().toString().replace("-", "");
                                GraphBox<T> graphBox = propertyProducer.createPropertyBox(skin, id, popupX, popupY);
                                graphContainer.addGraphBox(graphBox, "Property", true, popupX, popupY);
                            }
                        });
                propertyMenu.addItem(valueMenuItem);
            }
            propertyMenuItem.setSubMenu(propertyMenu);
            popupMenu.addItem(propertyMenuItem);
        }

        return popupMenu;
    }

    private void createSubMenu(final float popupX, final float popupY, PopupMenu popupMenu, String menuName,
                               Set<GraphBoxProducer<T>> producers) {
        MenuItem valuesMenuItem = new MenuItem(menuName);
        PopupMenu valuesMenu = new PopupMenu();
        for (final GraphBoxProducer<T> producer : producers) {
            final String name = producer.getTitle();
            if (uiGraphConfiguration.isAddableGraphBox(producer)) {
                MenuItem valueMenuItem = new MenuItem(name);
                valueMenuItem.addListener(
                        new ClickListener(Input.Buttons.LEFT) {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                String id = UUID.randomUUID().toString().replace("-", "");
                                GraphBox<T> graphBox = producer.createDefault(skin, id);
                                graphContainer.addGraphBox(graphBox, name, true, popupX, popupY);
                            }
                        });
                valuesMenu.addItem(valueMenuItem);
            }
        }
        valuesMenuItem.setSubMenu(valuesMenu);
        popupMenu.addItem(valuesMenuItem);
    }

    private PopupMenu createPropertyPopupMenu(float x, float y) {
        PopupMenu menu = new PopupMenu();
        for (Map.Entry<String, PropertyBoxProducer<T>> propertyEntry : uiGraphConfiguration.getPropertyBoxProducers().entrySet()) {
            final String name = propertyEntry.getKey();
            final PropertyBoxProducer<T> value = propertyEntry.getValue();
            MenuItem valueMenuItem = new MenuItem(name);
            valueMenuItem.addListener(
                    new ClickListener(Input.Buttons.LEFT) {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            PropertyBox<T> defaultPropertyBox = value.createDefaultPropertyBox(skin);
                            addPropertyBox(skin, name, defaultPropertyBox);
                        }
                    });
            menu.addItem(valueMenuItem);
        }

        return menu;
    }

    public String getId() {
        return id;
    }

    @Override
    public GraphBox<T> getNodeById(String id) {
        return graphContainer.getGraphBoxById(id);
    }

    @Override
    public Iterable<? extends GraphBox<T>> getNodes() {
        return graphContainer.getGraphBoxes();
    }

    public GraphContainer<T> getGraphContainer() {
        return graphContainer;
    }

    private void updatePipelineValidation() {
        GraphValidator.ValidationResult<GraphBox<T>, GraphConnection, PropertyBox<T>, T> validationResult = GraphValidator.validateGraph(this, "end");
        graphContainer.setValidationResult(validationResult);
        if (validationResult.hasErrors()) {
            validationLabel.setColor(Color.RED);
            validationLabel.setText("Invalid");
        } else if (validationResult.hasWarnings()) {
            validationLabel.setColor(Color.YELLOW);
            validationLabel.setText("Acceptable");
        } else {
            validationLabel.setColor(Color.GREEN);
            validationLabel.setText("OK");
        }
    }

    private VerticalGroup createPropertiesUI(final Skin skin) {
        final VerticalGroup pipelineProperties = new VerticalGroup();
        pipelineProperties.grow();
        Table headerTable = new Table();
        headerTable.setBackground(skin.getDrawable("vis-blue"));
        headerTable.add(new Label("Properties", skin)).growX();
        final VisTextButton newPropertyButton = new VisTextButton("Add", "menu-bar");
        newPropertyButton.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        PopupMenu popupMenu = createPropertyPopupMenu(x, y);
                        popupMenu.showMenu(pipelineProperties.getStage(), newPropertyButton);
                    }
                });
        headerTable.add(newPropertyButton);
        headerTable.row();
        pipelineProperties.addActor(headerTable);
        return pipelineProperties;
    }

    public void addPropertyBox(Skin skin, String type, final PropertyBox<T> propertyBox) {
        propertyBoxes.add(propertyBox);
        final Actor actor = propertyBox.getActor();

        final Table table = new Table(skin);
        final Drawable window = skin.getDrawable("window");
        BaseDrawable wrapper = new BaseDrawable(window) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                window.draw(batch, x, y, width, height);
                ;
            }
        };
        wrapper.setTopHeight(3);
        table.setBackground(wrapper);
        table.add(new Label(type, skin)).growX();
        VisImageButton removeButton = new VisImageButton("close-window");
        removeButton.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        removePropertyBox(propertyBox);
                    }
                });
        table.add(removeButton);
        table.row();
        table.add(actor).colspan(2).growX();
        table.row();
        pipelineProperties.addActor(table);

        contentTable.fire(new GraphChangedEvent(true));
    }

    private void removePropertyBox(PropertyBox<T> propertyBox) {
        Actor actor = propertyBox.getActor();
        propertyBoxes.remove(propertyBox);
        pipelineProperties.removeActor(actor.getParent());

        contentTable.fire(new GraphChangedEvent(true));
    }

    @Override
    public void dispose() {
        graphContainer.dispose();
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public Table getContentTable() {
        return contentTable;
    }

    public JSONObject serializeGraph() {
        JSONObject pipeline = new JSONObject();

        JSONArray objects = new JSONArray();
        Vector2 tmp = new Vector2();
        graphContainer.getCanvasPosition(tmp);
        for (GraphBox<T> graphBox : graphContainer.getGraphBoxes()) {
            Window window = graphContainer.getBoxWindow(graphBox.getId());
            JSONObject object = new JSONObject();
            object.put("id", graphBox.getId());
            object.put("type", graphBox.getType());
            object.put("x", tmp.x + window.getX());
            object.put("y", tmp.y + window.getY());

            JSONObject data = graphBox.serializeData();
            if (data != null)
                object.put("data", data);

            objects.add(object);
        }
        pipeline.put("nodes", objects);

        JSONArray connections = new JSONArray();
        for (GraphConnection connection : graphContainer.getConnections()) {
            JSONObject conn = new JSONObject();
            conn.put("fromNode", connection.getNodeFrom());
            conn.put("fromField", connection.getFieldFrom());
            conn.put("toNode", connection.getNodeTo());
            conn.put("toField", connection.getFieldTo());
            connections.add(conn);
        }
        pipeline.put("connections", connections);

        JSONArray properties = new JSONArray();
        for (PropertyBox propertyBox : propertyBoxes) {
            JSONObject property = new JSONObject();
            property.put("name", propertyBox.getName());
            property.put("type", propertyBox.getType().name());

            JSONObject data = propertyBox.serializeData();
            if (data != null)
                property.put("data", data);

            properties.add(property);
        }
        pipeline.put("properties", properties);

        return pipeline;
    }
}
