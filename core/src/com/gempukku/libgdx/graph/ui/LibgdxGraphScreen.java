package com.gempukku.libgdx.graph.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineFieldType;
import com.gempukku.libgdx.graph.shader.ShaderFieldType;
import com.gempukku.libgdx.graph.ui.graph.GetSerializedGraph;
import com.gempukku.libgdx.graph.ui.graph.GraphDesignTab;
import com.gempukku.libgdx.graph.ui.graph.RequestGraphOpen;
import com.gempukku.libgdx.graph.ui.graph.SaveCallback;
import com.gempukku.libgdx.graph.ui.pipeline.UIPipelineConfiguration;
import com.gempukku.libgdx.graph.ui.shader.UIShaderConfiguration;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.OptionDialogListener;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

public class LibgdxGraphScreen extends Table {
    private Map<String, JSONObject> savedGraphs = new HashMap<>();
    private FileHandle editedFile;
    private final TabbedPane tabbedPane;
    private GraphDesignTab<PipelineFieldType> graphDesignTab;
    private Skin skin;
    private final Table insideTable;

    private MenuItem save;
    private MenuItem saveAs;
    private MenuItem exportShader;
    private MenuItem close;

    public LibgdxGraphScreen(Skin skin) {
        this.skin = skin;
        setFillParent(true);
        insideTable = new Table(skin);

        tabbedPane = new TabbedPane();
        tabbedPane.addListener(
                new TabbedPaneAdapter() {
                    @Override
                    public void switchedTab(Tab tab) {
                        insideTable.clearChildren();
                        insideTable.add(tab.getContentTable()).grow().row();

                        save.setDisabled(false);
                        saveAs.setDisabled(false);
                        close.setDisabled(false);

                        GraphDesignTab<?> designTab = (GraphDesignTab<?>) tab;
                        boolean graphShaderTab = designTab.getType() == GraphDesignTab.Type.Graph_Shader;
                        exportShader.setDisabled(!graphShaderTab);
                    }

                    @Override
                    public void removedAllTabs() {
                        save.setDisabled(true);
                        saveAs.setDisabled(true);
                        close.setDisabled(true);
                        exportShader.setDisabled(true);
                    }
                });

        MenuBar menuBar = createMenuBar();
        add(menuBar.getTable()).growX().row();
        add(tabbedPane.getTable()).left().growX().row();
        add(insideTable).grow().row();
    }

    private void openShaderTab(String id, JSONObject shader) {
        UIShaderConfiguration shaderConfiguration = new UIShaderConfiguration();
        final GraphDesignTab<ShaderFieldType> shaderTab = GraphLoader.loadGraph(shader, new UIGraphLoaderCallback<ShaderFieldType>(
                skin, new GraphDesignTab<ShaderFieldType>(true, GraphDesignTab.Type.Graph_Shader, id, "Shader", skin,
                shaderConfiguration, new SaveCallback<ShaderFieldType>() {
            @Override
            public void save(GraphDesignTab<ShaderFieldType> graphDesignTab) {
                savedGraphs.put(graphDesignTab.getId(), graphDesignTab.serializeGraph());
                graphDesignTab.setDirty(true);
            }
        }), shaderConfiguration));
        tabbedPane.add(shaderTab);
        tabbedPane.switchTab(shaderTab);
        shaderTab.setDirty(false);
    }

    private Tab findTabByGraphId(String id) {
        for (Tab tab : tabbedPane.getTabs()) {
            if (tab instanceof GraphDesignTab) {
                GraphDesignTab graphDesignTab = (GraphDesignTab) tab;
                if (graphDesignTab.getId().equals(id))
                    return tab;
            }
        }
        return null;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = createFileMenu();

        menuBar.addMenu(fileMenu);
        return menuBar;
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu("File");

        MenuItem newMenuItem = new MenuItem("New from template");
        newMenuItem.setSubMenu(createTemplateMenu());
        fileMenu.addItem(newMenuItem);

        MenuItem open = new MenuItem("Open");
        open.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        open();
                    }
                });
        fileMenu.addItem(open);

        save = new MenuItem("Save");
        save.setDisabled(true);
        save.setShortcut(Input.Keys.CONTROL_LEFT, Input.Keys.S);
        save.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        save();
                    }
                });
        fileMenu.addItem(save);

        saveAs = new MenuItem("Save As");
        saveAs.setDisabled(true);
        saveAs.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        saveAs();
                    }
                });
        fileMenu.addItem(saveAs);

        fileMenu.addSeparator();

        exportShader = new MenuItem("Export shader");
        exportShader.setDisabled(true);
        exportShader.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        exportShader();
                    }
                });
        fileMenu.addItem(exportShader);

        fileMenu.addSeparator();

        close = new MenuItem("Close pipeline");
        close.setDisabled(true);
        close.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        closePipeline();
                    }
                });
        fileMenu.addItem(close);

        fileMenu.addSeparator();
        MenuItem exit = new MenuItem("Exit");
        exit.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        closeApplication();
                    }
                });
        fileMenu.addItem(exit);

        return fileMenu;
    }

    private void closeApplication() {
        if (graphDesignTab != null && graphDesignTab.isDirty()) {
            Dialogs.showOptionDialog(getStage(), "Pipeline modified",
                    "Current pipeline has been modified, would you like to save it?",
                    Dialogs.OptionDialogType.YES_NO, new OptionDialogListener() {
                        @Override
                        public void yes() {
                            save();
                            Gdx.app.exit();
                        }

                        @Override
                        public void no() {
                            Gdx.app.exit();
                        }

                        @Override
                        public void cancel() {

                        }
                    });
        } else {
            Gdx.app.exit();
        }
    }

    private void closePipeline() {
        if (graphDesignTab != null && graphDesignTab.isDirty()) {
            Dialogs.showOptionDialog(getStage(), "Pipeline modified",
                    "Current pipeline has been modified, would you like to save it?",
                    Dialogs.OptionDialogType.YES_NO, new OptionDialogListener() {
                        @Override
                        public void yes() {
                            save();
                            removeAllTabs();
                            savedGraphs.clear();
                            graphDesignTab = null;
                        }

                        @Override
                        public void no() {
                            removeAllTabs();
                            savedGraphs.clear();
                            graphDesignTab = null;
                        }

                        @Override
                        public void cancel() {

                        }
                    });
        } else {
            removeAllTabs();
            savedGraphs.clear();
            graphDesignTab = null;
        }
    }

    private void open() {
        if (graphDesignTab != null && graphDesignTab.isDirty()) {
            Dialogs.showErrorDialog(getStage(), "Current pipeline has been modified, close it or save it");
        } else {
            removeAllTabs();

            FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
            fileChooser.setModal(true);
            fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
            fileChooser.setListener(new FileChooserAdapter() {
                @Override
                public void selected(Array<FileHandle> file) {
                    FileHandle selectedFile = file.get(0);
                    loadPipelineFromFile(selectedFile);
                    editedFile = selectedFile;
                    graphDesignTab.setDirty(false);
                }
            });
            getStage().addActor(fileChooser.fadeIn());
        }
    }

    private void exportShader() {
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                FileHandle selectedFile = file.get(0);
                if (!selectedFile.name().toLowerCase().endsWith(".json")) {
                    selectedFile = selectedFile.parent().child(selectedFile.name() + ".json");
                }
                writeGraph(selectedFile, ((GraphDesignTab<?>) tabbedPane.getActiveTab()).serializeGraph());
            }
        });
        getStage().addActor(fileChooser.fadeIn());
    }

    private void saveAs() {
        FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
        fileChooser.setModal(true);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> file) {
                FileHandle selectedFile = file.get(0);
                if (!selectedFile.name().toLowerCase().endsWith(".json")) {
                    selectedFile = selectedFile.parent().child(selectedFile.name() + ".json");
                }
                editedFile = selectedFile;
                saveToFile(graphDesignTab, selectedFile);
            }
        });
        getStage().addActor(fileChooser.fadeIn());
    }

    private void save() {
        if (graphDesignTab != null) {
            if (editedFile == null) {
                FileChooser fileChooser = new FileChooser(FileChooser.Mode.SAVE);
                fileChooser.setModal(true);
                fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
                fileChooser.setListener(new FileChooserAdapter() {
                    @Override
                    public void selected(Array<FileHandle> file) {
                        FileHandle selectedFile = file.get(0);
                        if (!selectedFile.name().toLowerCase().endsWith(".json")) {
                            selectedFile = selectedFile.parent().child(selectedFile.name() + ".json");
                        }
                        editedFile = selectedFile;
                        saveToFile(graphDesignTab, selectedFile);
                    }
                });
                getStage().addActor(fileChooser.fadeIn());
            } else {
                saveToFile(graphDesignTab, editedFile);
            }
        }
    }

    private void saveToFile(GraphDesignTab<?> graphDesignTab, FileHandle editedFile) {
        for (Tab tab : tabbedPane.getTabs()) {
            tab.save();
        }
        JSONObject graph = graphDesignTab.serializeGraph();

        writeGraph(editedFile, graph);

        graphDesignTab.setDirty(false);
    }

    private void writeGraph(FileHandle editedFile, JSONObject graph) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(editedFile.write(false));
            try {
                graph.writeJSONString(out);
                out.flush();
            } finally {
                out.close();
            }
        } catch (IOException exp) {
            exp.printStackTrace();
        }
    }

    private PopupMenu createTemplateMenu() {
        PopupMenu templateMenu = new PopupMenu();
        MenuItem menuItem = new MenuItem("Empty");
        menuItem.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        loadPipelineFromFile(Gdx.files.internal("template/empty-pipeline.json"));
                    }
                });
        templateMenu.addItem(menuItem);
        return templateMenu;
    }

    private void loadPipelineFromFile(FileHandle fileHandle) {
        if (graphDesignTab != null && graphDesignTab.isDirty()) {
            Dialogs.showErrorDialog(getStage(), "Current pipeline has been modified, close it or save it");
        } else {
            removeAllTabs();
            try {
                InputStream stream = fileHandle.read();
                try {
                    UIPipelineConfiguration graphConfiguration = new UIPipelineConfiguration();
                    graphDesignTab = GraphLoader.loadGraph(stream, new UIGraphLoaderCallback<PipelineFieldType>(
                            skin, new GraphDesignTab<PipelineFieldType>(false, GraphDesignTab.Type.Render_Pipeline, "main", "Render pipeline", skin,
                            graphConfiguration, null), graphConfiguration));

                    graphDesignTab.getContentTable().addListener(
                            new EventListener() {
                                @Override
                                public boolean handle(Event event) {
                                    if (event instanceof RequestGraphOpen) {
                                        RequestGraphOpen request = (RequestGraphOpen) event;
                                        Tab tab = findTabByGraphId(request.getId());
                                        if (tab != null) {
                                            tabbedPane.switchTab(tab);
                                        } else {
                                            JSONObject graph = savedGraphs.get(request.getId());
                                            if (graph == null)
                                                graph = request.getJsonObject();
                                            openShaderTab(request.getId(), graph);
                                        }
                                        return true;
                                    }
                                    if (event instanceof GetSerializedGraph) {
                                        GetSerializedGraph request = (GetSerializedGraph) event;
                                        request.setGraph(savedGraphs.get(request.getId()));
                                        return true;
                                    }
                                    return false;
                                }
                            });

                    tabbedPane.add(graphDesignTab);
                    editedFile = null;
                } finally {
                    stream.close();
                }
            } catch (IOException exp) {
                throw new RuntimeException("Unable to load default pipeline definition", exp);
            }
        }
    }

    private void removeAllTabs() {
        tabbedPane.removeAll();
        insideTable.clearChildren();
    }

    public void dispose() {
        for (Tab tab : tabbedPane.getTabs()) {
            tab.dispose();
        }
    }
}
