package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.ui.graph.GraphBox;
import com.gempukku.libgdx.graph.ui.graph.GraphContainer;
import com.gempukku.libgdx.graph.ui.graph.PopupMenuProducer;
import com.gempukku.libgdx.graph.ui.graph.PropertyBasedPopupMenuProducer;
import com.gempukku.libgdx.graph.ui.graph.PropertyProducer;
import com.gempukku.libgdx.graph.ui.pipeline.GraphDesignTab;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBox;
import com.gempukku.libgdx.graph.ui.pipeline.PropertyBoxProducer;
import com.gempukku.libgdx.graph.ui.producer.GraphBoxProducer;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import org.json.simple.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UIPipelineLoaderCallback implements PipelineLoaderCallback<GraphDesignTab<PipelineFieldType>> {
    private Skin skin;
    private GraphDesignTab<PipelineFieldType> graphDesignTab;
    private GraphContainer<PipelineFieldType> graphContainer;

    public UIPipelineLoaderCallback(Skin skin) {
        this.skin = skin;
    }

    @Override
    public void start() {
        graphDesignTab = new GraphDesignTab<PipelineFieldType>(skin, createGraphPopupMenuProducer(), createPropertyPopupMenuProducer());
        graphContainer = graphDesignTab.getGraphContainer();
    }

    private PropertyBasedPopupMenuProducer<PipelineFieldType> createGraphPopupMenuProducer() {
        return new PropertyBasedPopupMenuProducer<PipelineFieldType>() {
            @Override
            public PopupMenu createPopupMenu(Iterable<? extends PropertyProducer<PipelineFieldType>> propertyProducers, final float popupX, final float popupY) {
                PopupMenu popupMenu = new PopupMenu();

                for (Map.Entry<String, Set<GraphBoxProducer<PipelineFieldType>>> producersEntry : UIPipelineConfiguration.graphBoxProducers.entrySet()) {
                    String menuName = producersEntry.getKey();
                    Set<GraphBoxProducer<PipelineFieldType>> producer = producersEntry.getValue();
                    createSubMenu(popupX, popupY, popupMenu, menuName, producer);
                }

                if (propertyProducers.iterator().hasNext()) {
                    MenuItem propertyMenuItem = new MenuItem("Property");
                    PopupMenu propertyMenu = new PopupMenu();
                    for (final PropertyProducer<PipelineFieldType> propertyProducer : propertyProducers) {
                        final String name = propertyProducer.getName();
                        MenuItem valueMenuItem = new MenuItem(name);
                        valueMenuItem.addListener(
                                new ClickListener(Input.Buttons.LEFT) {
                                    @Override
                                    public void clicked(InputEvent event, float x, float y) {
                                        String id = UUID.randomUUID().toString().replace("-", "");
                                        GraphBox<PipelineFieldType> graphBox = propertyProducer.createPropertyBox(skin, id, popupX, popupY);
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
        };
    }

    private void createSubMenu(final float popupX, final float popupY, PopupMenu popupMenu, String menuName, Set<GraphBoxProducer<PipelineFieldType>> producers) {
        MenuItem valuesMenuItem = new MenuItem(menuName);
        PopupMenu valuesMenu = new PopupMenu();
        for (final GraphBoxProducer<PipelineFieldType> producer : producers) {
            final String name = producer.getTitle();
            if (!UIPipelineConfiguration.notAddableProducers.contains(producer)) {
                MenuItem valueMenuItem = new MenuItem(name);
                valueMenuItem.addListener(
                        new ClickListener(Input.Buttons.LEFT) {
                            @Override
                            public void clicked(InputEvent event, float x, float y) {
                                String id = UUID.randomUUID().toString().replace("-", "");
                                GraphBox<PipelineFieldType> graphBox = producer.createDefault(skin, id);
                                graphContainer.addGraphBox(graphBox, name, true, popupX, popupY);
                            }
                        });
                valuesMenu.addItem(valueMenuItem);
            }
        }
        valuesMenuItem.setSubMenu(valuesMenu);
        popupMenu.addItem(valuesMenuItem);
    }

    private PopupMenuProducer createPropertyPopupMenuProducer() {
        return new PopupMenuProducer() {
            @Override
            public PopupMenu createPopupMenu(float x, float y) {
                PopupMenu menu = new PopupMenu();
                for (Map.Entry<String, PropertyBoxProducer<PipelineFieldType>> propertyEntry : UIPipelineConfiguration.propertyProducers.entrySet()) {
                    final String name = propertyEntry.getKey();
                    final PropertyBoxProducer<PipelineFieldType> value = propertyEntry.getValue();
                    MenuItem valueMenuItem = new MenuItem(name);
                    valueMenuItem.addListener(
                            new ClickListener(Input.Buttons.LEFT) {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    PropertyBox<PipelineFieldType> defaultPropertyBox = value.createDefaultPropertyBox(skin);
                                    graphDesignTab.addPropertyBox(skin, name, defaultPropertyBox);
                                }
                            });
                    menu.addItem(valueMenuItem);
                }

                return menu;
            }
        };
    }


    @Override
    public void addPipelineNode(String id, String type, float x, float y, JSONObject data) {
        GraphBoxProducer<PipelineFieldType> producer = findProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find pipeline producer for type: " + type);
        GraphBox<PipelineFieldType> graphBox = producer.createPipelineGraphBox(skin, id, data);
        graphContainer.addGraphBox(graphBox, producer.getTitle(), producer.isCloseable(), x, y);
    }

    @Override
    public void addPipelineVertex(String fromNode, String fromProperty, String toNode, String toProperty) {
        graphContainer.addGraphConnection(fromNode, fromProperty, toNode, toProperty);
    }

    @Override
    public void addPipelineProperty(String type, String name, JSONObject data) {
        PropertyBoxProducer<PipelineFieldType> producer = findPropertyProducerByType(type);
        if (producer == null)
            throw new IllegalArgumentException("Unable to find property producer for type: " + type);
        PropertyBox<PipelineFieldType> propertyBox = producer.createPropertyBox(skin, name, data);
        graphDesignTab.addPropertyBox(skin, type, propertyBox);
    }

    @Override
    public GraphDesignTab<PipelineFieldType> end() {
        graphContainer.adjustCanvas();
        return graphDesignTab;
    }

    private static PropertyBoxProducer<PipelineFieldType> findPropertyProducerByType(String type) {
        for (PropertyBoxProducer<PipelineFieldType> producer : UIPipelineConfiguration.propertyProducers.values()) {
            if (producer.supportsType(type))
                return producer;
        }

        return null;
    }

    private static GraphBoxProducer<PipelineFieldType> findProducerByType(String type) {
        for (Set<GraphBoxProducer<PipelineFieldType>> producers : UIPipelineConfiguration.graphBoxProducers.values()) {
            for (GraphBoxProducer<PipelineFieldType> producer : producers) {
                if (producer.getType().equals(type))
                    return producer;
            }
        }
        return null;
    }
}
