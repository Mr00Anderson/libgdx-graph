package com.gempukku.libgdx.graph.ui.pipeline;

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
import com.gempukku.libgdx.graph.ui.AwareTab;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.graph.ui.graph.GraphChangedListener;
import com.gempukku.libgdx.graph.ui.graph.GraphContainer;
import com.gempukku.libgdx.graph.ui.graph.PopupMenuProducer;
import com.gempukku.libgdx.graph.ui.graph.PropertyBasedPopupMenuProducer;
import com.gempukku.libgdx.graph.ui.preview.PreviewWidget;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTextButton;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class GraphDesignTab<T extends FieldType> extends AwareTab implements Graph<GraphBox<T>, GraphConnection, T> {
    private List<PropertyBox<T>> propertyBoxes = new LinkedList<>();

    private final VerticalGroup pipelineProperties;
    private final GraphContainer<T> graphContainer;
    private PopupMenuProducer propertyPopupMenuProducer;

    private Table contentTable;
    private Label validationLabel;

    public GraphDesignTab(Skin skin, final PropertyBasedPopupMenuProducer<T> graphPopupMenuProducer, PopupMenuProducer propertyPopupMenuProducer) {
        super(true, false);

        contentTable = new Table(skin);
        pipelineProperties = createPropertiesUI(skin);

        graphContainer = new GraphContainer<T>(
                new PopupMenuProducer() {
                    @Override
                    public PopupMenu createPopupMenu(float x, float y) {
                        return graphPopupMenuProducer.createPopupMenu(propertyBoxes, x, y);
                    }
                });
        this.propertyPopupMenuProducer = propertyPopupMenuProducer;
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
    public GraphBox<T> getNodeById(String id) {
        return graphContainer.getGraphBoxById(id);
    }

    @Override
    public Iterable<? extends GraphConnection> getIncomingConnections(String nodeId) {
        return graphContainer.getIncomingConnections(getNodeById(nodeId));
    }

    @Override
    public Iterable<String> getAllGraphNodes() {
        return Iterables.transform(graphContainer.getGraphBoxes(),
                new Function<GraphBox<T>, String>() {
                    @Override
                    public String apply(@Nullable GraphBox graphBox) {
                        return graphBox.getId();
                    }
                });
    }

    public GraphContainer<T> getGraphContainer() {
        return graphContainer;
    }

    private void updatePipelineValidation() {
        GraphValidator.ValidationResult<GraphBox<T>, GraphConnection, T> validationResult = GraphValidator.validateGraph(this, "end");
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
        headerTable.add(new Label("Pipeline properties", skin)).growX();
        final VisTextButton newPropertyButton = new VisTextButton("Add", "menu-bar");
        newPropertyButton.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        PopupMenu popupMenu = propertyPopupMenuProducer.createPopupMenu(x, y);
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
        return "Pipeline";
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
        pipeline.put("objects", objects);

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
